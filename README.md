# Hoffman-file-compression-program

霍夫曼文件压缩程序

利用霍夫曼编码对文件进行压缩与解压

2018年3月项目

学习`Java`以来实现的第一个的项目，尚未对`Java`语言工具类以及`JVM`模型有所了解，代码编写有很多不足之处。

不足之处1：直接对`String`类型对象进行拼接或剪切  
  >>`String`类型对象为不可变对象，存放于方法区的字符串常量池，每次拼接或剪切极有可能产生新的字符串放入常量池中，而极大程度地影响程序的性能。
  代码中多次使用了字符串的拼接，创建了大量的字符串，致使永久代空间不足而触发`Full GC`(老年代与永久代空间不足都会触发`Full GC`，而新生代空间不足则会触发`Minor GC`)，导致程序运行缓慢，效率低下。  
    
  解决方法：使用`StringBuilder`进行拼接与剪切操作  
  >>`StringBuilder`与`StringBuffer`实际上都是对同一个对象进行操作，而不会创建新的对象，从而节约内存空间。`StringBuilder`与`StringBuffer`的底层“扩容”原理都是通过调用`Arrays`的`copyOf()`方法中的`System.arraycopy()`方法来实现的，`arraycopy()`方法为`native`方法，执行效率高。这里采用`StringBuilder`而不是`StringBuffer`的原因，是因为`StringBuffer`是线程安全的，拥有`synchronized`关键字修饰，在多线程下有更好的效果。但是在此程序中，单线程情况下`synchronized`关键字会导致每次调用`StringBuffer`中的方法时自动获得与释放锁(实质上是`JVM`发出`monitorenter`与`monitorexit`的指令去获取与释放对象头(每一个`Java`对象都拥有一个对象头，对象头中包含了本对象的相关信息，包括锁的状态信息)中的监视器`ObjectMonitor`，其含有的`_owenr`字段用来记录当前获取锁的线程)，而造成资源的浪费，效率相较`StringBuilder`低下，这也是降低本程序执行效率的第二个因素。
  
不足之处2：可变数组采用`Vector`  
  >>与上面的`StringBuffer`类似，`Vector`同样是一个线程安全的工具，每个方法上面都拥有`synchronized`关键字来修饰，执行效率相对较低。 
    
  解决办法：可以采用`Arraylist`  
  >>与`Vector`不同，`Arraylist`的方法上面没有`synchronized`关键字修饰，不会出现大量的锁的获取与释放而造成资源浪费的情况，可以提高运行效率。`Arraylist`的底层“扩容”原理同样是由`Arrays`的`copyOf()`方法中的`System.arraycopy()`方法来实现的。
