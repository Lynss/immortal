package com.ly.immortal

//使用as可以改变函数名字、、、
import com.ly.immortal.BaseTest
import java.text.SimpleDateFormat
import org.junit.Test as t
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.ReentrantLock
import kotlin.collections.ArrayList

//const
const val MASTER = "Long Yun"

//expand function this 可以隐藏，下面两段代码等价
//fun String.lastChar():Char = this.get(this.length-1)
//fun String.lastChar():Char = get(length-1)
//同样可以利用扩展属性获取最后一个字符，如果是StringBuffer的话可以设置set功能
val String.lastChar: Char
    get() = get(length - 1)

@JvmOverloads
fun <T> Collection<T>.joinToString(
        separator: String = ",",
        prefix: String = "[",
        postfix: String = "]"): String {
    val result = StringBuilder(prefix)
    for ((index, element) in this.withIndex()) {
        if (index > 0) result.append(separator)
        result.append(element)
    }
    result.append(postfix)
    return result.toString()
}

fun Collection<String>.join() = joinToString(separator = "ssss")

data class Person(val name: String, var marry: Boolean)

data class Rectangle(val height: Int, val width: Int) {
    val isSquare: Boolean get() = height == width
    var testSet: Int = 1

}

enum class Color(val r: Int, val g: Int, var b: Int) {
    RED(255, 0, 0),
    GREEN(0, 255, 0),
    BLUE(0, 0, 255);//必要的分号

    fun rgb() = (r * 255 + g) * 255 + b
}

fun main(args: Array<String>) {
    val b = testKotlin.Bank(100.0, 100)
    var i = 0
    while (i < 100) {
        val thread = Thread {

            b.transfer((Math.random() * 100).toInt(), (Math.random() * 100).toInt(), Math.random() * 100)
            Thread.sleep((Math.random() * 500).toLong())
        }
        thread.start()
        i++
    }
}


//fun main(args: Array<String>) {
//    val t1 = Thread {
//        Thread.sleep(1000)
//        println("000")
//    }
//    val t2 = Thread { println("111") }
//
//    t1.start()
//    t2.start()
//
//}

/**
 * java风格的代码
 * kotlin中的if类似三元表达式，后面的值可以直接返回
 * 聪明的我早已猜到2333
 */
interface Expr

class Num(val value: Int) : Expr
class Sum(val left: Expr, val right: Expr) : Expr
class testExption : Expr

fun eval(e: Expr): Int =
        if (e is Num)
            e.value
        else if (e is Sum)
            eval(e.left) + eval(e.right)
        else
            throw IllegalArgumentException("unexpected argument")

/**
 * evalWhen
 */
fun evalWhen(e: Expr) = when (e) {
    is Num -> e.value
    is Sum -> eval(e.left) + eval(e.right)
    else -> throw IllegalArgumentException("unexpected argument")
}


//如果when中没有对应的条件，会提示你加上else。。。醉醉的，编译时出现- -有点智能
fun getColor(color: Color) = when (color) {
    Color.BLUE -> "BLUE"
    Color.RED -> "RED"
    Color.GREEN -> "GREEN"
}

fun beGreen(color: Color) = when (color) {
    Color.GREEN -> "sad"
    Color.RED, Color.BLUE -> "233"
    else -> "ha?"
}

//默认val
fun mix(c1: Color, c2: Color) = when (setOf(c1, c2)) {
    setOf(Color.GREEN, Color.BLUE) -> "red"
    setOf(Color.GREEN, Color.RED) -> "blue"
    else -> "ha?"
}

fun whenWithoutParams(c1: Color, c2: Color) = when {
    c1 == Color.BLUE -> println("1get")
    c2 == Color.BLUE -> println("2get")
    else -> println("sa le ba ")
}

val oneToTen: IntRange = 1..10

class testKotlin : BaseTest() {
    fun max(a: Int, b: Int) = if (a > b) a else b

    @t
    fun test1() {
        println("fuck,world")
    }

    @t
    fun test2() {
        println("${max(2, 1)}")
    }

    @t
    fun test3() {
        val a = 1
        val b = arrayListOf("java")
        var c = 3
        b.add("kotlin")
        //a = 2 false
        //c = "2" true
    }

    @t
    fun test4() {
        val person = Person("a", true)
        println("personName:${person.name},married:${person.marry}")
    }

    @t
    fun test5() {
        val rectangle = Rectangle(2, 3)
        println("${rectangle.testSet}and${rectangle.isSquare}")
        rectangle.testSet = 5
        println("${rectangle.testSet}and${rectangle.isSquare}")
    }

    @t
    fun test6() {
        println(getColor(Color.GREEN))
        println(mix(Color.RED, Color.BLUE))
    }

    @t
    fun test7() {
        //方便在函数有默认值时调用
        println(beGreen(color = Color.GREEN))//居然还阔以这样写、、、我也是试出来的
    }

    @t
    fun test8() {
        whenWithoutParams(Color.BLUE, Color.BLUE)
    }

    @t
    fun test9() {
        //will throw a exception :unexpected argument
        println(eval(Sum(Sum(testExption(), Num(2)), Num(4))))
        println(eval(Sum(Sum(Num(1), Num(2)), Num(4))))
        println(evalWhen(Sum(Sum(Num(1), Num(2)), Num(4))))
        println(10 in oneToTen)
        println(10 in 0 until 10)
        println(10 in 0 downTo 10)
        for (i in 10 downTo 0) {
            print("$i ;")
        }

    }

    @t
    fun test10() {
        val a = TreeMap<Char, String>()
        for (c in 'A'..'F') {
            val b = Integer.toBinaryString(c.toInt())
            a[c] = b
        }
        for ((d, b) in a) {
            println("$d:$b")
        }
    }

    @t
    fun test11() {
        val list = arrayListOf(10, 11, "a")
        for ((index, elemnt) in list.withIndex()) {
            println("list[$index]:$elemnt:${list[index]}")
        }

    }

    @t
    fun test12() {
        println("kotlin" in "java".."scala")
        println("kotlin" in setOf("java", "scala"))
        println("kotlin" in setOf("java", "kotlin", "scala"))
    }

    //exception
    @t
    fun test13() {
        val number = try {
            Integer.parseInt("a132")
        } catch (e: NumberFormatException) {
            null
        } finally {
            //使用表达式方式try时finally中表达式无用单是return依旧执行
            //return
            //3
        }
        println(number ?: "NullPointException")
    }

    //collection
    @t
    fun test14() {
        val kSet = setOf(1, 2, 3, "3", "3")
        val kList = listOf(1, 2, 3, 3)
        val map = mapOf(1 to 1, 2 to 2, 3 to 3, 3 to 3)
        //[[1, 2, 3, 3]] not listToSet but a list set
        val listToSet = setOf(kList)
        //[1, 2, 3]
        //[1, 2, 3, 3]
        // {1=1, 2=2, 3=3}
        println(kSet)
        println(kSet.joinToString())
        println(kSet.joinToString(prefix = "{", postfix = "}"))
        println(kList.max())
        println(kList)
        println(map)
        println(listToSet)

        println(Set::class)
        println(kList.javaClass)
        println(map::class.java)
    }

    @t
    fun test15() {
        println("sbJava".lastChar)
    }

    @t
    fun t16() {
        var args: Array<Any> = arrayOf("2", 1)
        val list = listOf(*args) // 1 展开操作符解压(unpack)数组内容
        println(list)
    }

    @t
    fun t17() {
        val kotlinLogo = """| //
                            .|//
                            .|/ \"""
        println(kotlinLogo.trimMargin("."))

    }

    class User(val id: Int, val name: String, val address: String)

    fun User.validateBeforeSave() {
        fun validate(value: String, fieldName: String) {
            if (value.isEmpty()) {
                throw IllegalArgumentException(
                        "Can't save user $id: empty $fieldName") // 1 你可以直接访问User对象的属性
            }
        }

        validate(name, "Name")
        validate(address, "Address")
    }

    @t
    fun test18() {
        val user = User(1, "", "")
        user.validateBeforeSave()  // 2 调用扩展函数
        // Save user to the database
    }

    @t
    fun test19() {
        val a = ArrayList<Int>()
        a.add(2)
        a.add(3)
        a.add(0, 1)
        val b = arrayOfNulls<Int>(a.size)
        a.toArray(b)
        println(b[0])
    }

    @t
    fun test20() {
        val a = 1
        val b = 1
        println(a == b)
    }

    @t
    fun test21() {
        val a = Color.BLUE
    }

    @t
    fun testReduce() {
        val a = arrayOf(1, 2, 3, 4, 5)
        println(a.reduce { a, b -> a + b })

    }

    data class Bank(private val initMoney: Double, private val accountNum: Int) {
        private val accounts = DoubleArray(accountNum, { initMoney })
        private val bankLock = ReentrantLock()
        private var condition = bankLock.newCondition()
        var total = accountNum * initMoney
        fun transfer(from: Int, to: Int, money: Double) {
            try {
                if (!bankLock.tryLock(100, TimeUnit.MILLISECONDS)) {
                    println("100毫秒内获取锁失败")
                }
                if (accounts[from] < money) {
                    println("账户$from 余额不足：${accounts[from]}")
                    condition.await(100, TimeUnit.MILLISECONDS)
                }
                accounts[from] -= money
                accounts[to] += money
                condition.signalAll()
                total = accounts.reduce { a, b -> a + b }
                println("账户$from 转钱$money 到账户$to ,银行总余额：$total")
            } catch (e: InterruptedException) {
                condition.signalAll()
            } finally {
                bankLock.unlock()
            }
        }

    }


    @t
    fun testRandom() {
        for (i in 0..50) {
            val a = Random().nextDouble()*10
            val b = Math.random()*10
            println("a:$a;b:$b")
        }
    }

    val SIMPLE_DATE_FORMAT_THREAD_LOCAL = ThreadLocal.withInitial { SimpleDateFormat("yyyy-MM-dd") }

}



