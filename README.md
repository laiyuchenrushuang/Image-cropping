# 图片相关

  1.图片裁剪 ，自己看代码去
  
  2.图片压缩，今天找的Lubin压缩，说下开启方式
  
    【添加依赖】
  
    implementation 'top.zibin:Luban:1.1.8'
    
    【子线程 异步开启方式】---------测试压缩还是很厉害（注意适合手机存储区间的图片，app文件里面的不得行）
    
                Luban.with(this)
                .load(IMAGE_PATH + "/" + "IMG_19700101_080543.jpg")  // load  压缩谁
                .ignoreBy(100)       //压缩后文件大小范围内 单位K（size the value of file size, unit KB, default 100K）
                .setTargetDir(IMAGE_PATH)                //压缩后的保存文件路径
                .setRenameListener(object : OnRenameListener {  //重写压缩文件名
                    override fun rename(filePath: String?): String {
                        var file = File(filePath)
                        showLog("1 " + filePath)  //绝对路径
                        showLog("2 " + file.parentFile.path) //文件目录
                        showLog("3 "+file.name)  //file名字
                        return "IMG_19700101_080543.jpg"
                    }
                })
                .setCompressListener(object : OnCompressListener {  //压缩过程
                    override fun onSuccess(file: File?) {
                        showLog(""+file.path) // 谁被压缩成功了  
                        showToast("结束")
                    }
                    override fun onError(e: Throwable?) {
                        showToast("出错")
                    }
                    override fun onStart() {
                        showToast("开始")
                    }
                })
                .launch()   //启动
                
                var b = BitmapFactory.decodeFile(IMAGE_PATH+"/"+"laiyu.jpg")
                var b1 = BitmapFactory.decodeFile(IMAGE_PATH+"/"+"IMG_19700101_080543.jpg")
                
                showLog(" w = "+b.width)
                showLog(" h = "+b.height)
                showLog(" w1 = "+b1.width)
                showLog(" h1 = "+b1.height)
                
               ------- 但是图片大小被压缩了宽高 --------
               
           =========================================
           2020-04-16 18:10:05.440 2756-2756/? D/lylog: ---->  w = 720
           2020-04-16 18:10:05.441 2756-2756/? D/lylog: ---->  h = 1280
           2020-04-16 18:10:05.441 2756-2756/? D/lylog: ---->  w1 = 2160
           2020-04-16 18:10:05.441 2756-2756/? D/lylog: ---->  h1 = 3840
           =========================================


