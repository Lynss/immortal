package com.ly.immortal

import com.ly.immortal.BaseTest
import com.ly.immortal.testJDefault
import org.apache.log4j.Logger
import java.lang.reflect.AccessibleObject
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy
import java.util.*
import java.util.concurrent.ForkJoinPool
import java.util.concurrent.ForkJoinTask
import java.util.concurrent.RecursiveTask
import org.junit.Test as t

//扩张方法是不能进行override的
open class View(val a: String) {
    private val LOGGER = Logger.getLogger(testJDefault::class.java)
    open fun say() {
        println("1$a")
    }
}

fun View.show() {
    println('w')//char转String
}

fun Button.show() = println("fuck")

class Button : View("1") {
    override fun say() {
        println("2$a")
    }
}

class TestK : BaseTest() {
    @t
    fun t17() {
        val view: View = Button()
//        val button:Button = View() false
        view.say()//21
        view.show()//w
    }

    fun max(vararg a: Double): Double {
        var b= doubleArrayOf(*a)
        var c= arrayOf(1.0,2.0)
        var max = Double.MIN_VALUE
        for (d in a) {
            if (d > max) {
                max = d
            }
        }
        return max
    }

    @t
    fun test4() {
        println(max(2.1, 3.1, 4.5, 3.3))
        val doubles = doubleArrayOf(1.1, 2.3)
        println(max(*doubles))
        println(max(1.1,2.3))
    }

    @t
    fun test5() {
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
    fun test6() {
        val e = listOf(Employee("ly",10.0))
        val eClass = e.javaClass.componentType
        val eF = eClass.declaredFields
        val eM = eClass.declaredMethods
        val eC = eClass.declaredConstructors
        val eN = eClass.getDeclaredField("name")
        //
        AccessibleObject.setAccessible(arrayOf(eN), true)

        eN.isAccessible=true
        val wTF:String = eN.get(e) as String
        println(wTF)
        for (i in eF) {
            println(i)
        }
    }
    open class Employee(var name: String, var salary: Double) :Cloneable
    class Manager(name: String, salary: Double, private val sex: Int) : TestK.Employee(name, salary)

    @t
    fun test7() {
        Employee("1", 1.0).salary = 1.11

        val e = Employee("ly",10.0)
    }

    class TraceHandler(val target: Any) : InvocationHandler {
        override fun invoke(proxy: Any, method: Method, args: Array<Any>?): Any {
            print(target)
            print("." + method.name + "(")
            if (args != null) {
                print(args[0])
            }
            println(");")
            return method.invoke(target, *args?: arrayOf())
        }
    }

    @t
    fun test8() {
        val elements = arrayOfNulls<Any>(1000)
        for (i in elements.indices) {
            var target = i+1
            elements[i] = Proxy.newProxyInstance(null, arrayOf(Comparable::class.java), {
                proxy: Any, method: Method, args: Array<Any>? ->
                println("$target.${method.name}(${if(args!=null)args[0] else ""});")
                method.invoke(target , *args?: arrayOf())
            })
        }
        val key = Random().nextInt(elements.size) + 1
        val result = Arrays.binarySearch(elements, key)
        if (result >= 0) println(elements[result])

    }

    @t
    fun test9() {
        val className = Employee::class.java
        val method=className.getMethod("getName")
        println(method.invoke(Employee("ly",2.2)))
    }

    @t
    fun test10() {
        //            String b = "a";
        //        assert Integer.valueOf(b) > 0 : b;
        val b = "a"
        assert(Integer.valueOf(b) > 0) { b }
        println(b)
    }

    class Pair<E>(var first: E, var second: E)

    fun <T : Comparable<T>> minmax(vararg args: T): Pair<T> {
        var a = Arrays.stream(args).max(Comparator.comparing<T, T> { it}).get()
        return Pair(args.maxBy { it }!!,
                args.minBy { it }!!)
    }

    @t
    fun testT() {
        val a = minmax(2.1, 3.2, 3.4)
        println(a.first.toString() + a.second.toString())
    }

    @t
    fun testT2() {
        val a = Pair("2","2")
        val b = Pair(2,2)
        if (a.javaClass == b.javaClass) {
            println("wtf")
        }
        if (a::class == b::class) {
            println("wtf")
        }

    }

    fun <T> addAll(collection: MutableCollection<T>, vararg ts: T) {
        for (t in ts) {
            collection.add(t)
        }
        //        collection.addAll(Arrays.asList(ts));
    }

    @t
    fun testVarargs() {
        val a = arrayOf<Pair<String>>()
        val c = arrayOf<Pair<String>>()
        val b = ArrayList<Pair<String>>()
        //  Pair<String>[] d = new Pair<String>[1]; false
        val collection = object : ArrayList<Pair<String>>() {
            private val serialVersionUID = 4030367313679797324L

            init {
                add(Pair("1", "1"))
            }
        }
        addAll(collection, Pair("1", "1"), Pair("2", "2"))
        addAll(collection, *a)
        addAll(collection, *b.toTypedArray())
    }

    @t
    fun testIterator() {
        val a = mutableListOf(1, 2, 3)
        val iterator = a.listIterator()
        a.forEach(::println)
        val b = iterator.next()
        iterator.set(100)
        println(b)
        val c = iterator.next()
        println(c)
        println(iterator.remove())//如果是listOf是不允许修改的,并且不能连续的remove
        a.forEach(::println)
        iterator.add(10)//如果是listOf是不允许修改的
        iterator.add(11)//如果是listOf是不允许修改的
        a.forEach(::println)
    }

    @t
    fun testMapEntry() {
        val a = mapOf(1 to 1, 2 to 2)
        val b = a.entries
        for (entry in b) {
            println("${entry.key}:${entry.value}")
        }
    }

    @t
    fun testCollectionAndMap() {
        val a = kotlin.Pair(1, 2)
        val b = 1
        val c = Collections.singletonMap(1,1)
        val d = Collections.singleton(1)
        val e = Collections.singletonList(1)
        var g = e.subList(0, 1)
    }

    class Counter(private val from: Int, private val to: Int, private val doubles: DoubleArray) : RecursiveTask<Double>() {

        override fun compute(): Double? {
            return if (to - from < 1000000) {
                var count = 0.0
                (from until to)
                        .asSequence()
                        .filter { doubles[it] > 0.9 }
                        .forEach { count += doubles[it] }
                count
            } else {
                val mid = (from + to) / 2
                val before = Counter(from, mid, doubles)
                val after = Counter(mid, to, doubles)
                ForkJoinTask.invokeAll(before, after)
                before.join() + after.join()
            }
        }

        companion object {
            private val serialVersionUID = 682761188345201245L
        }
    }

    @t
    fun testForkJoin() {
        val randoms = DoubleArray(10000000)
        for (i in randoms.indices) {
            randoms[i] = Math.random()
        }
        val forkJoinPool = ForkJoinPool()
        val counter = Counter(0, 10000000, randoms)
        forkJoinPool.invoke(counter)
        println(counter.join())
        val c = randoms.filter { random -> random > 0.9 }.reduce { a, b -> a + b }
        println(c)
    }

    @t
    fun testSequence() {
        val a =  (1 until 10)
                .asSequence()
                .filter { it > 5 }
        val b =  (1 until 10)
                .filter { it > 5 }
        for (i in a) {
            println(i)
        }

        println(a.reduce({c,d->c+d}))
        println(b.reduce({c,d->c+d}))
    }

    @t
    fun testPath() {
        println(System.getProperty("user.dir"))
    }

    private fun <T> cast(collection: Collection<T>): List<T> {
        val list = ArrayList<T>()
        list.addAll(collection)
        return list
    }
}