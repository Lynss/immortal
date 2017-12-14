package com.ly.immortal

import sun.misc.Lock
import java.io.*
import java.util.*
import kotlin.reflect.full.memberProperties
import org.junit.Test as ly
import kotlin.coroutines.experimental.*
import kotlin.streams.toList

fun findB(b: KotlinInAction.Person) = b.name == "b"

fun KotlinInAction.Person.isPerson(name: String) = this.name == name

inline fun <T> synchronized(lock: Lock, action: () -> T): T {
    lock.lock()
    try {
        return action()
    } finally {
        lock.unlock()
    }
}

inline fun <reified T : Any> Any.isA() = if (this is T) {
    println("yes")
} else {
    println("no")
}

inline fun <reified T> loadService() = ServiceLoader.load(T::class.java)


class KotlinInAction : BaseTest() {
    //data类能自动生成toString，equals,hashCode等方法的重载
    data class Person(val name: String, val age: Int = 18) {
        infix fun introduce(greeting: String) = "I am ${this.name},$greeting"
    }

    fun Array<out Person>.findPerson(finder: (Person) -> Boolean) = firstOrNull { finder(it) }
    val findC = { c: Person ->
        c.name == "c"
    }

    var isB = ::findB

    @ly
    fun testFindPerson() {
        val d = arrayOf(Person("a"), Person("c"), Person("b"))
        val a = d.findPerson { it.name == "a" }
        val c = d.findPerson(findC)
        val b = d.findPerson(::findB)
        val b1 = d.findPerson { findB(it) }
        if (b != null) {
            println(b introduce "fuck")

        }
        println(c?.introduce("hello"))
    }


    @ly
    fun testTail() {
        var counter = 0
        tailrec infix fun Int.needTimesAddTo(end: Int): Int {
            val temp = this + 1
            counter++
            return if (temp < end) {
                temp.needTimesAddTo(end)
            } else {
                counter
            }
        }

        val begin = 0
        println(begin needTimesAddTo(10))
    }

    //下面两种写法等价,所以val放到构造其中是一种语法糖的写法，能同时为同名属性赋值
    //记住应当尽量使用默认值来取代重载
    class PrivateClass1 private constructor(val test: String)

    class PrivateClass2 private constructor(test: String) {
        val test: String

        init {
            this.test = test
        }
    }

    //封装类，所有的子类必须继承该封装类,必须为嵌套类（不是inner内部类，即不具有父类的引用），不能声明为data类
    sealed class Shape() {
        class Rectangle() : Shape() {

        }

        class Circle() : Shape() {

        }
    }

    //类委托模式，关键词：by,自动实现被委托类的所有方法，只需要自己去修改想要修改的地方就ok
    data class CounterList<T>(private val innerList: MutableCollection<T> = ArrayList()) : MutableCollection<T> by innerList {
        var count = 0
        override fun add(element: T): Boolean {
            count++
            return innerList.add(element)
        }

        override fun addAll(elements: Collection<T>): Boolean {
            count += elements.size
            return innerList.addAll(elements)
        }
    }

    @ly
    fun testBy() {
        val counterList = CounterList<String>()
        counterList.add("a")
        counterList.addAll(listOf("b", "b"))
        println(counterList.count)
        println(counterList)
    }

    //KOTLIN中使用对象声明来起到java中单例的作用，关键词object，但是一个对象声明不能有任何构造方法
    object ShoppingCart {
        val commodityList = arrayListOf<Int>()
        fun caculatePrice() = commodityList.reduce { first, second -> first + second }
    }

    @ly
    fun testObjectSingle() {
        ShoppingCart.commodityList.add(1_0000)
        println(ShoppingCart.caculatePrice())
    }

    //使用object关键词可以在类的内部创建一个单例的嵌套对象(不含类的实例)，但是如果想要访问类的私有属性，达到类似java中static的作用，可以使用伴生对象companion object,!!!同时，外部类的名字会作为伴生对象的实例
    class CompanionA {
        companion object {
            fun bar() {
                println("companion object called")
            }
        }
    }

    //同时，object可以用于创建匿名内部类，但是如果在匿名内部类只需要实现一个但方法的接口时，可以用lambda代替匿名内部类
    @ly
    fun testCompanionA() {
        CompanionA.bar()
    }

    //成员引用和方法引用,注意isPerson中两个参数居然是先后放入的
    val getPersonName = Person::name
    val isPerson = Person::isPerson
    @ly
    fun testQuote() {
        println(getPersonName(Person("meme")))
        println(isB(Person("b")))
        val c = Person("c")
        val isPersonC = c::isPerson
        println(isPerson(c, "c"))
        println(isPersonC("c"))
    }

    @ly
    fun testBreak() {
        outside@ for (i in 1..10) {
            inside@ for (j in 1..5) {
                println("$i:$j")
                if (j == 3) {
//                    break@inside
                    break@outside
                }
            }
            println("memeda")
        }
    }

    @ly
    fun testMapMapValues() {
        val a = mapOf(0 to "o", 1 to "l")
        println(a.mapValues { it.value.toUpperCase() })
    }

    @ly
    fun testGroupBy() {
        val a = listOf(Person("A"), Person("A"), Person("b"))
        val groupBy = a.groupBy { it.name }
        println(groupBy)
    }

    @ly
    fun testFlatMap() {
        val a = listOf("abc", "abc", "cde")
        println(a.flatMap { it.toList() }.toSet())
    }

    //因为在集合做临时操作时每一步操作都会生成一个临时的集合，如果操作很多或者说集合很大时，可以通过将集合转换为sequence序列来避免中间集合的产生.sequence是惰性的，他的值在被观测时才会计算出来.但是，在学习内联函数后会发现，使用sequence时lambda没有进行内联，所以才说只在大的集合和中间操作很多的时候使用asSequence
    @ly
    fun testAsSequence() {
        val a = listOf(1, 2, 3)
        val b = a.asSequence()
        val c = b.filter {
            println(it)
            it >= 2
        }.map {
            println(it)
            10 * it
        }
        println("=========")
        for (i in c) {
            println(i)
        }
    }

    //sam构造方法,可以显示的将lambda表达式转换为对应的函数接口实现
    fun createSam(): Runnable = Runnable { println("test sam") }

    @ly
    fun testSam() {
        createSam().run()
    }

    //with(和js中的with有些像)可以接受两个参数，第二个lambda是一个带接受者的lambda，接受第一个参数作为lambda体中this所指向的对象
    fun withLog(): String = with(StringBuilder()) {
        for (i in 'A'..'Z') {
            append(i)
        }
        append("\nNow i know how to use with")
        println(toString())
        toString()
    }

    @ly
    fun testWith() {
        println(withLog())
    }

    //apply 和with类似，但是with返回的是lambda执行的结果，而apply会返回接受者对象本身
    fun applyLog(): StringBuilder = StringBuilder().apply {
        for (i in 'A'..'Z') {
            append(i)
        }
        append("\nNow i know how to use with")
        println(toString())
    }

    @ly
    fun testApply() {
        println(applyLog().toString())
    }

    @ly
    fun testApplyInit() {
        val a = ArrayList<String>().apply {
            addAll(arrayOf("a", "b"))
        }
    }

    //as?运算符,可以在转换失败是返回null
    @ly
    fun testAS() {
        val a = "1aaa" as? Int ?: println("Illegal cast")
        println("complete")
    }

    //let可以将可空变量传给要求非空参数的函数
    fun sendEmailFrom(email: String) {
        println("send email from $email")
    }

    @ly
    fun testLet() {
        var email: String? = "me@gmail.com"
        email?.let { sendEmailFrom(it) }
        email = null
        email?.let { sendEmailFrom(it) }
    }
    //可以通过为泛型制定一个不为空的类型来防止参数为空

    //只读集合和可修改集合大概知道，不过值得注意的是!!只读集合是有可能改变的，因为可能一个集合同时被两种类型的集合所引用

    //创建数字数组的方法
    @ly
    fun testArrayInit() {
        val a = IntArray(20) {
            it
        }
        a.forEach { println(it) }
        val b = listOf(1, 2, 3)
        println("%s/%s/%s".format(*(b.toTypedArray())))
    }

    //kotlin中，你可以通过operor操作符来重载运算符，不要同时定义两个
    data class Point(var x: Int, var y: Int) {
        operator fun plus(other: Point) = Point(this.x + other.x, this.y + other.y)
        //        operator fun plusAssign(other: Point) {
//            this.x+=other.x
//            this.y+=other.y
//        }
        operator fun get(a: String): Int = when (a) {
            "x" -> this.x
            "y" -> this.y
            else -> throw IllegalArgumentException()
        }
    }

    @ly
    fun testPlusOperatorPoint() {
        val a = Point(1, 2)
        val b = Point(3, 4)
        println((a + b)["x"])
        val (c, d) = a
        println("$c:$d")
    }

    //对于那些很耗资源的操作，你有可能需要使用支持属性来进行惰性初始化，很像缓存的写法
    class PersonEmail(val name: String) {
        private var _emails: List<String>? = null
        var emails: List<String>
            get() {
                if (_emails == null) {
                    _emails = listOf("1", "2")
                }
                return _emails!!
            }
            set(value) {
                _emails = value
            }

    }

    //这种写法不是线程安全的，而且基本等价于下面的写法，可以看到，emails实际上是一个代理，当想访问emails的时候，实际上提供的是lazy中lambda的返回（也即是上面的_emails）
    class PersonEmailLazy(val name: String) {
        val eamils by lazy { listOf("1", "2") }
    }

    //使用inline内联函数能消除lambda带来的额外开销,还可以使用noinline来标记那些不需要进行内联的lambda
    @ly
    fun testInlineLock() {
        val l = Lock()
        synchronized(l) {
            println("aha")
        }
    }

    //!!值得注意的是必须要在被内联的lambda中才能使用return，很好理解不是么，内联函数会被进行替换，所以在内联函数中使用return相当于调用
    // lambda的函数的return，当然，你也可以使用标签返回(如果不加标记时可以用lambda函数的名称来作为标记，如foreach)
    fun inlineReader(): String? {
        val a = BufferedReader(FileReader("/workspace/ly/immortal/src/test/kotlin/com/ly/immortal/1.txt")).use mark@ { br ->
//            val c = br.readText()
            val d = br.lines().toList()
            val a = br.readLine()
            println(a)
            val b = br.readLine()
            println(b)
            return@mark d
//            return@forEach b
        }
        println(a)
        return "1"
    }

    @ly
    fun testInlineReader() {
        println(inlineReader())
    }

    //匿名函数，和js中非常类似,与lambda不同的是匿名函数只会从匿名函数本身返回，很好理解~和js中匿名函数一致，匿名函数也能被内联~
    val a = fun(c: Int) {

    }

    @ly
    fun testAnonymous() {
        val a = listOf(1, "2")
        for((c,d)in a.withIndex()){

        }
        a.forEach(fun(a) {
            println(a)
        })
        val b = a.filter(fun(a): Boolean {
            println(a)
            return a == 1
        })
        println(b)
    }

    //当泛型需要多个约束时
    fun <T> ensureTrailingPeriod(seq: T) where T : Appendable, T : CharSequence {
        if (!seq.endsWith('.')) {
            seq.append('.')
        }
    }

    //当你创建泛型类时，如果不创建约束，会默认使用Any?作为约束
    class Processor<in T> {
        fun process(value: T) {
            value?.hashCode()
        }
    }

    @ly
    fun testDefaultGenericity() {
        val a = Processor<Unit?>()
        a.process(null)
    }

    fun printSum(immutableCollection: Collection<*>) {
        val a = immutableCollection as? List<Int> ?: throw IllegalArgumentException("cant cast to list<Int>")
        println("cast end")
        println(a.sum())
    }

    @ly
    fun testCastAndSum() {
        //cast end
//        val b = listOf("A","B")
//        printSum(b)
        //cant cast
        val c = setOf("A", "B")
        printSum(c)
    }

    //正如上面测试的结果，kotlin在默认情况下也是会擦除函数的类型;此外可以通过inline内联函数来实化函数的的类型参数,原理就是因为内联函数会在运行时将字节码插入被调用的地方，因此，实际上，内联函数的泛型类型是已经被确定了的
    @ly
    fun testInlineGenericity() {
        val a = listOf(1, 2, 3)
        val c = listOf("1")
        val d = c::class
        val b = a::class
        if (d.isInstance(c)) {
            println("jeje")
        }
        a.isA<List<String>>()//true.证明最终类型仍然被擦除了
    }

    //kotlin中，可变集合和不可操作集合是不一样的，如果A是B的子类型，那么对于不可操作集合而言,Collection<A>也是Collection<B>的子类型
    //对于这种类或者接口，我们称其为协变的，一个协变的类或者接口能够保留父子类的关系
    @ly
    fun testSecurityWithCollectionGenericity() {
        fun addSth(list: MutableCollection<Any>) {
            list.add(11)
        }

        fun readSth(list: Collection<Any>) {
            list.forEach { println(it) }
        }

        val a = listOf(1)
        readSth(a)

        val b = mutableListOf("2")
//        addSth(b) cant do like this ,it cant compile
        addSth(mutableListOf("1"))
    }

    //参照上面，如果我们想声明一个类型是可以协变的，需要用到out关键字
    open class Animal(open val name: String) {
        infix fun feedBy(a: Person) {
            println("${name} has been feed by ${a.name}")
        }
    }

    fun feedAll(a: MutableList<Animal>, b: Person) {
        a.forEach { it feedBy b }
    }

    fun feedAllWithOut(a: List<Animal>, b: Person) {
        a.forEach { it feedBy b }
    }

    class Herd<T : Animal>(private val animals: MutableCollection<T> = mutableListOf()) : MutableCollection<T> by animals {
        infix fun feedAllBy(a: Person) {
            animals.forEach { it feedBy a }
        }

    }

    class Cat(override val name: String) : Animal(name)

    //out 位置意味着这个类型只能是被返回的而不能作为被修改的
    @ly
    fun testOut() {
        val b = mutableListOf(Cat("tom"), Cat("jerry"))
        val a = Herd(b)
        a feedAllBy Person("ly")

//        feedAll(b,Person("ly")) cant compile for mutableList isn't covariant
        feedAllWithOut(b, Person("ly"))
    }
    //有协变就同样有逆变，in关键词用于形容那些只用于参数而不用于返回类型的（mutableList不属于逆变也不属于协变）对于逆变而言，如果A是B的子类型,那么Sth<B>就是Sth<A>的子类型，例如Comparator
    //对于一个高阶函数而言，(T)->R中在参数类型上逆变，而在返回类型R上协变

    //点变型，普通写法：
    fun <T : R, R> copyTo(source: MutableList<T>, copy: MutableList<R>) {
        source.forEach { copy.add(it) }
    }

    //优雅的写法 ,out表示只用其在out位置，in同理,同时out也意味着必须是T的子类型，而in必须是T的父类型（这里其实不需要in,但是写出来更容易理解）
    fun <T> copyToGentle(source: MutableList<out T>, copy: MutableList<in T>) {
        source.forEach { copy.add(it) }
    }

    @ly
    fun testKotlinReflect() {
        val a = Person::class
        println(a.qualifiedName)
        val b = Person("ly")
        val c = b.javaClass.kotlin
        c.memberProperties.forEach {
            println(it.name)
        }
    }

    @ly
    fun testFunction() {
        val a = ::sum
        val x = { a: Int, b: Int -> a + b }
        val y = fun(x: Int, y: Int) = x + y
        x.invoke(1, 2)

        val b = a.invoke(1, 2)
        val c = a.call(1, 2)
        val d = a(1, 2)
        val e = ::counter
        println(e.call())
        println(e.invoke())
        e.setter.call(1)
        println(e.get())
    }

    infix fun String.endWith(buildAction: StringBuilder.() -> String): String {
        val a = StringBuilder(this)
        return a.buildAction()
    }

    @ly
    fun testLambdaWithParam() {
        val a = "balala"
        println(a endWith {
            this.append("fuck")
            this.toString()
        })
    }

    open class Tag
    class TABLE : Tag() {
        fun tr(init :TR.()->Unit){

        }
    }

    class TR : Tag() {
        fun td(init:TD.()->Unit){

        }
    }
    class TD : Tag() {

    }

    fun table(init:TABLE.()->Unit) = TABLE().apply(init)
    fun tr(init:TR.()->Unit)=TR().apply(init)

    @ly
    fun testKHtmlDsl() {

    }

    @ly
    fun testVarargs() {
        val a = arrayOf(1, 2, 3)
        val (b,c,d) = listOf(*a)
    }

    @ly
    fun testFib() {
        val fibCreater = buildSequence{
            var a = 0
            var b = 1
            yield(1)
            while (true) {
                var temp = a+b
                yield(temp)
                a = b
                b = temp
            }
        }
        println(fibCreater.take(7).toList())
    }

    @ly
    fun testUserDir() {
        println(System.getProperties())
    }

    @ly
    fun testO() {
        val f = File("/workspace/ly/immortal/src/test/kotlin/com/ly/immortal/1.txt")
        FileWriter(f,true).use {
            it.write("1")
        }

    }

    @ly
    fun testI() {
        val f = File("/workspace/ly/immortal/src/test/kotlin/com/ly/immortal/1.txt")
        DataInputStream(FileInputStream(f)).use {
            do {
                println(it.read())
            }while (it.read()!=-1)
        }

    }
}

fun main(args: Array<String>) {
    BufferedReader(InputStreamReader(System.`in`)).use {
        do{
            println(it.readLine())
        }while (it.readLine()!="end")
    }
}


fun sum(x: Int, y: Int): Int {
    println(x + y)
    return x + y
}

var counter = 0