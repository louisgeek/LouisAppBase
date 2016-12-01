package com.louisgeek.louisappbase.imagepicker;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.louisgeek.louisappbase.lrucache.LruCacheBitmapUitl;
import com.louisgeek.louisappbase.util.ImageUtil;
import com.louisgeek.louisappbase.util.ReflectUtil;
import com.louisgeek.louisappbase.util.RegexUtil;
import com.louisgeek.louisappbase.util.ScreenUtil;
import com.socks.library.KLog;

import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

import static com.louisgeek.louisappbase.util.ReflectUtil.getFieldValue;

/**
 * Created by louisgeek on 2016/11/16.
 */

public class KooImagesLoader {
    //双重检查 单例模式
    private static volatile KooImagesLoader instance;

    /* 私有构造方法，防止被实例化 */
    private KooImagesLoader(int threadCount, QueueType queueType) {
        mThreadCount = threadCount;
        mQueueType = queueType;
        initPoolThread();
    }

    private KooImagesLoader() {
        initPoolThread();
    }

    public static KooImagesLoader getInstance() {
        if (instance == null) {
            synchronized (KooImagesLoader.class) {
                if (instance == null) {
                    instance = new KooImagesLoader();
                }
            }
        }
        return instance;
    }

    public static KooImagesLoader getInstance(int threadCount, QueueType queueType) {
        if (instance == null) {
            synchronized (KooImagesLoader.class) {
                if (instance == null) {
                    instance = new KooImagesLoader(threadCount, queueType);
                }
            }
        }
        return instance;
    }

    /**
     * 轮询的线程
     */
    private Thread mPoolThread;
    private Handler mPoolThreadHandler;

    private LinkedList<Runnable> mRunnableTasks ;
    /**
     * 定长线程池，可控制线程最大并发数，超出的线程会在队列中等待
     */
    private ExecutorService mExecutorServicePool;
    /**
     * 引入一个值为1的信号量，防止mPoolThreadHander未初始化完成
     */
    private volatile Semaphore mSemaphore_Handler = new Semaphore(0);
    /**
     * 引入一个信号量，由于线程池内部也有一个阻塞线程，防止加入任务的速度过快，使LIFO效果不明显
     */
    private volatile Semaphore mSemaphore_Pool;
    /**
     * 队列的默认调度方式  后进先出
     */
    private QueueType mQueueType = QueueType.LIFO;
    private int mThreadCount = 1;//线程池中线程的默认数量

    /**
     * 队列的调度方式
     */
    public enum QueueType {
        //Last-In First-Out   ,First-In First-Out
        LIFO, FIFO
    }

    private class ImageSize {
        int width;
        int height;
    }

    private class ImageBitmapInfo {
        Bitmap bitmap;
        ImageView imageView;
        String path;
    }

    private void initPoolThread() {

        KLog.d("initPoolThread");
        // 后台轮询线程
        mPoolThread = new Thread(new Runnable() {
            @Override
            public void run() {

                //在本线程创建Looper实例并维护MessageQueue
                Looper.prepare();
                KLog.d("prepare after");
                //在子线程初始化
                mPoolThreadHandler = new Handler() {
                    //Handler构造方法中，会首先得到当前线程中保存的Looper实例，
                    // 进而与Looper实例中的MessageQueue关联
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);


                        try {
                            mSemaphore_Pool.acquire();
                        } catch (InterruptedException e) {
                        }
                        /**
                         * 取出一个任务，然后放入线程池去执行
                         */
                        Runnable runnable = getRunnableTask();
                        mExecutorServicePool.execute(runnable);
                    }
                };

                // mPoolThreadHandler实例化完 释放1个信号量
                mSemaphore_Handler.release();
                KLog.d("loop before");
                Looper.loop();//让当前线程进入一个无限循环，不断从MessageQueue中读取消息， 然后回调msg.target.dispatchMessage(msg)方法


            }
        });
        // 后台轮询线程启动
        //mPoolThread
        mPoolThread.start();


        mExecutorServicePool = Executors.newFixedThreadPool(mThreadCount);
        mRunnableTasks = new LinkedList<>();
        mSemaphore_Pool = new Semaphore(mThreadCount);

    }

    /**
     * 添加一个任务
     */
    private synchronized void addRunnableTask(Runnable runnable) {
        mRunnableTasks.add(runnable);
        try {
            if (mPoolThreadHandler == null) {
                //acquire一个信号量，其实就是去等待mPoolThreadHander初始化完成
                mSemaphore_Handler.acquire();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //发消息给后台线程，让它去取出一个任务执行
        mPoolThreadHandler.sendEmptyMessage(0);
    }

    /**
     * 通过队列的调度方式
     * 取出一个任务
     */
    private synchronized Runnable getRunnableTask() {
        if (mQueueType == QueueType.FIFO) {
            KLog.d("FIFOFIFOFIFO");
            return mRunnableTasks.removeFirst();
        } else if (mQueueType == QueueType.LIFO) {
           // KLog.d("LIFO");
            return mRunnableTasks.removeLast();
        }
        return null;

    }

    /**
     * UI Handler
     */
    private Handler mUiHandler;

    /**
     * 加载图片
     *
     * @param path
     * @param imageView
     */
    public void loadImage(final String path, final ImageView imageView) {
        LruCacheBitmapUitl.initFirst(imageView.getContext());
        // set tag
        imageView.setTag(path);

        if (mUiHandler==null){
        mUiHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {

                ImageBitmapInfo imageBitmapInfo = (ImageBitmapInfo) msg.obj;
                ImageView imageView = imageBitmapInfo.imageView;
                Bitmap bitmap = imageBitmapInfo.bitmap;
                String path = imageBitmapInfo.path;
                // 将path与getTag存储路径进行比较  防止滑动时发生图片混乱
                if (imageView.getTag().toString().equals(path)) {
                    imageView.setImageBitmap(bitmap);
                }

            }
        };}
        /**
         *内存
         */
        Bitmap bitmap = LruCacheBitmapUitl.getCacheFromMemory(path);
        if (bitmap != null) {
            //内存中有  直接显示

            ImageBitmapInfo imageBitmapInfo = new ImageBitmapInfo();
            imageBitmapInfo.bitmap = bitmap;
            imageBitmapInfo.imageView = imageView;
            imageBitmapInfo.path = path;

            Message message = mUiHandler.obtainMessage();
            message.obj = imageBitmapInfo;
            mUiHandler.sendMessage(message);

        } else {
            /**
             *  //内存中没有 加入队列
             */
            this.addRunnableTask(buildRunnableTask(path, imageView, true));
        }

    }


    /**
     * 根据传入的参数，新建一个任务
     *
     * @param imageView
     * @return
     */
    private Runnable buildRunnableTask(final String pathOrUrl, final ImageView imageView, final boolean enableDisk) {
        // KLog.d("buildRunnableTask");

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap;
                if (RegexUtil.checkURL(pathOrUrl)) {
                    KLog.d("网络图片" + pathOrUrl);
                    //网络图片
                    bitmap = LruCacheBitmapUitl.getCacheFromDisk(pathOrUrl);
                    if (bitmap == null) {
                        //硬盘里也没有缓存
                        ImageSize imageSize = getWishImageSize(imageView);
                        int reqWidth = imageSize.width;
                        int reqHeight = imageSize.height;
                        //
                        bitmap = ImageUtil.downloadImageToBitmapWishImageSize(pathOrUrl, reqWidth, reqHeight);
                        //
                        LruCacheBitmapUitl.saveCacheToDisk(pathOrUrl, bitmap);
                    }

                } else {
                    //本地图片
                    // KLog.d("本地图片"+pathOrUrl);
                    bitmap = zoomBitmapFromPath(pathOrUrl, imageView);

                }

                // 把图片加入到内存缓存
                LruCacheBitmapUitl.saveCacheToMemory(pathOrUrl, bitmap);

                //显示
                ImageBitmapInfo imageBitmapInfo = new ImageBitmapInfo();
                imageBitmapInfo.bitmap = bitmap;
                imageBitmapInfo.imageView = imageView;
                imageBitmapInfo.path = pathOrUrl;

                Message message = mUiHandler.obtainMessage();
                message.obj = imageBitmapInfo;
                mUiHandler.sendMessage(message);

                //释放信号量
                mSemaphore_Pool.release();
            }
        };
        return runnable;
    }

    private Bitmap zoomBitmapFromPath(final String path, final ImageView imageView) {
        // 加载图片
        // 图片的压缩
        // 1、获得图片需要显示的大小
        ImageSize imageSize = this.getWishImageSize(imageView);

        int reqWidth = imageSize.width;
        int reqHeight = imageSize.height;
        KLog.d("reqWidth:" + reqWidth);
        KLog.d("reqHeight:" + reqHeight);
        // 2、压缩图片
        Bitmap bitmap = ImageUtil.decodeSampledBitmapFromPath(path, reqWidth,
                reqHeight);

        return bitmap;
    }

    /**
     * 根据ImageView获得适当的压缩的宽和高
     * <p>
     * 首先企图通过getWidth获取显示的宽；有些时候，这个getWidth返回的是0；
     * 那么我们再去看看它有没有在布局文件中书写宽；
     * 如果布局文件中也没有精确值，那么我们再去看看它有没有设置最大值；
     * 如果最大值也没设置，那么我们只有拿出我们的终极方案，使用我们的屏幕宽度；
     * 总之，不能让它任性，我们一定要拿到一个合适的显示值。
     * 可以看到这里或者最大宽度，我们用的反射，而不是getMaxWidth()；维萨呢，因为getMaxWidth竟然要API 16，我也是醉了；为了兼容性，
     *
     * @param imageView
     * @return
     */
    @Deprecated
    private ImageSize getWishImageSize(ImageView imageView) {
        ImageSize imageSize = new ImageSize();
        int screenWidth = ScreenUtil.getScreenWidth(imageView.getContext());
        int screenHeight = ScreenUtil.getScreenHeight(imageView.getContext());

        final ViewGroup.LayoutParams vlp = imageView.getLayoutParams();

        int width = imageView.getWidth();// 获取imageview的实际宽度
        if (width <= 0) {
            width = vlp.width;// 获取imageview在layout中声明的宽度
        }
        if (width <= 0) {
            // width = imageView.getMaxWidth();// 检查最大值
            try {
                width = ReflectUtil.getFieldValue(imageView, "mMaxWidth");
            } catch (Exception e) {
              //  e.printStackTrace();
                KLog.w(e.getMessage());
            }
//            width = ReflectUtil.getImageViewField(imageView, "mMaxWidth");
        }
        if (width <= 0) {
            width = screenWidth;
        }

        int height = imageView.getHeight();// 获取imageview的实际高度
        if (height <= 0) {
            height = vlp.height;// 获取imageview在layout中声明的宽度
        }
        if (height <= 0) {
            try {
                height = getFieldValue(imageView, "mMaxHeight");// 检查最大值
            } catch (Exception e) {
                e.printStackTrace();
            }
//            height = (int) ReflectUtil.getImageViewField(imageView, "mMaxHeight");// 检查最大值
        }
        if (height <= 0) {
            height = screenHeight;
        }

        //
        imageSize.width = width;
        imageSize.height = height;
        return imageSize;

    }


}