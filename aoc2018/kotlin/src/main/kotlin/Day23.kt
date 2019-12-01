import com.microsoft.z3.ArithExpr
import com.microsoft.z3.Context
import utility.Point3d
import utility.manhattanDistance
import java.nio.file.Files
import java.nio.file.Paths


data class Drone(val loc: Point3d, val strength: Long) {
    constructor(x: Long, y: Long, z: Long, strength: Long) : this(
        Point3d(
            x,
            y,
            z
        ), strength
    )
}

fun Drone.inRange(point: Point3d) =
    loc.manhattanDistance(point) <= strength

fun Drone.inRange(drone: Drone) =
    inRange(drone.loc)

object Day23 {
    private val re = Regex("""pos=<(-?\d+),(-?\d+),(-?\d+)>, r=(\d+)""")


    fun answer1(drones: Sequence<Drone>): Any {
        val biggest = drones.maxBy { it.strength }!!
        return drones.count { it.inRange(biggest) }
    }


    /*
    /Library/Java/JavaVirtualMachines/jdk-10.0.2.jdk/Contents/Home/bin/java "-javaagent:/Applications/IntelliJ IDEA CE.app/Contents/lib/idea_rt.jar=55984:/Applications/IntelliJ IDEA CE.app/Contents/bin" -Dfile.encoding=UTF-8 -classpath /Users/jbristow/git/adventofcode/aoc2018/kotlin/out/production/classes:/Users/jbristow/git/adventofcode/aoc2018/kotlin/out/production/resources:/Users/jbristow/.gradle/caches/modules-2/files-2.1/org.jetbrains.kotlin/kotlin-stdlib-jdk8/1.3.10/71d0fa967493eb76648b575edf1762cb2d0c7f10/kotlin-stdlib-jdk8-1.3.10.jar:/Users/jbristow/.gradle/caches/modules-2/files-2.1/org.jetbrains.kotlinx/kotlinx-coroutines-core/1.0.1/f33e8dab753f33d1bbb07cca664fd6f13d993d7e/kotlinx-coroutines-core-1.0.1.jar:/Users/jbristow/.gradle/caches/modules-2/files-2.1/io.reactivex.rxjava2/rxkotlin/2.2.0/e1e2fbb13b1c7dc6d2d03b6221a016d1501ad5a9/rxkotlin-2.2.0.jar:/Users/jbristow/.gradle/caches/modules-2/files-2.1/org.apache.tinkerpop/gremlin-driver/3.3.4/24b5185e45017e17e428b7274edacf38dd050b52/gremlin-driver-3.3.4.jar:/Users/jbristow/.gradle/caches/modules-2/files-2.1/org.apache.tinkerpop/gremlin-core/3.3.4/204adbe69e1fc0e31ed0b5c2976475ba0f462788/gremlin-core-3.3.4.jar:/Users/jbristow/.gradle/caches/modules-2/files-2.1/org.slf4j/slf4j-simple/1.8.0-beta2/17687308d20646a7ee9f9f8b9f0d376959248639/slf4j-simple-1.8.0-beta2.jar:/Users/jbristow/.gradle/caches/modules-2/files-2.1/io.github.microutils/kotlin-logging/1.6.22/cf9521760b48c02c3a7da92c6038891e9f7a137/kotlin-logging-1.6.22.jar:/Users/jbristow/.gradle/caches/modules-2/files-2.1/org.slf4j/jcl-over-slf4j/1.7.21/331b564a3a42f002a0004b039c1c430da89062cd/jcl-over-slf4j-1.7.21.jar:/Users/jbristow/.gradle/caches/modules-2/files-2.1/org.slf4j/slf4j-api/1.8.0-beta2/ba136e771a794f77ab41fa879706e5cbd5b20f39/slf4j-api-1.8.0-beta2.jar:/Users/jbristow/.gradle/caches/modules-2/files-2.1/org.apache.httpcomponents/httpclient/4.5.6/1afe5621985efe90a92d0fbc9be86271efbe796f/httpclient-4.5.6.jar:/Users/jbristow/.gradle/caches/modules-2/files-2.1/org.jetbrains.kotlin/kotlin-stdlib-jdk7/1.3.10/4d147bf43060dc43d61b096e24da1e67dfe0c032/kotlin-stdlib-jdk7-1.3.10.jar:/Users/jbristow/.gradle/caches/modules-2/files-2.1/org.jetbrains.kotlin/kotlin-stdlib/1.3.10/b178c1501609c6e4ee8be635513cb023a466457d/kotlin-stdlib-1.3.10.jar:/Users/jbristow/.gradle/caches/modules-2/files-2.1/org.jetbrains.kotlinx/kotlinx-coroutines-core-common/1.0.1/679991d1b388fd9392eeb3d63b10b3f4c284f389/kotlinx-coroutines-core-common-1.0.1.jar:/Users/jbristow/.gradle/caches/modules-2/files-2.1/org.apache.tinkerpop/gremlin-shaded/3.3.4/8f38814da9bb7847f249d97996c353a9bbae394f/gremlin-shaded-3.3.4.jar:/Users/jbristow/.gradle/caches/modules-2/files-2.1/commons-configuration/commons-configuration/1.10/2b36e4adfb66d966c5aef2d73deb6be716389dc9/commons-configuration-1.10.jar:/Users/jbristow/.gradle/caches/modules-2/files-2.1/commons-collections/commons-collections/3.2.2/8ad72fe39fa8c91eaaf12aadb21e0c3661fe26d5/commons-collections-3.2.2.jar:/Users/jbristow/.gradle/caches/modules-2/files-2.1/org.yaml/snakeyaml/1.15/3b132bea69e8ee099f416044970997bde80f4ea6/snakeyaml-1.15.jar:/Users/jbristow/.gradle/caches/modules-2/files-2.1/io.reactivex.rxjava2/rxjava/2.1.6/596a6b4752ca6ec511a167e92b6387654f32023b/rxjava-2.1.6.jar:/Users/jbristow/.gradle/caches/modules-2/files-2.1/org.javatuples/javatuples/1.2/507312ac4b601204a72a83380badbca82683dd36/javatuples-1.2.jar:/Users/jbristow/.gradle/caches/modules-2/files-2.1/com.carrotsearch/hppc/0.7.1/8b5057f74ea378c0150a1860874a3ebdcb713767/hppc-0.7.1.jar:/Users/jbristow/.gradle/caches/modules-2/files-2.1/com.jcabi/jcabi-manifests/1.1/e4f4488c0e3905c6fab287aca2569928fe1712df/jcabi-manifests-1.1.jar:/Users/jbristow/.gradle/caches/modules-2/files-2.1/com.squareup/javapoet/1.8.0/e858dc62ef484048540d27d36f3ec2177a3fa9b1/javapoet-1.8.0.jar:/Users/jbristow/.gradle/caches/modules-2/files-2.1/net.objecthunter/exp4j/0.4.8/cf1cfc0f958077d86ac7452c7e36d944689b2ec4/exp4j-0.4.8.jar:/Users/jbristow/.gradle/caches/modules-2/files-2.1/io.netty/netty-all/4.0.56.Final/26b28bcb33ec9e4286cd856321eea3df57c31aaf/netty-all-4.0.56.Final.jar:/Users/jbristow/.gradle/caches/modules-2/files-2.1/org.codehaus.groovy/groovy/2.4.15/2e8b9ed123776a690d3cfe0e7ebcc07d8cde433d/groovy-2.4.15-indy.jar:/Users/jbristow/.gradle/caches/modules-2/files-2.1/org.codehaus.groovy/groovy-json/2.4.15/2cf315d9c8ebfc50dc8621238e85019603c9cf1d/groovy-json-2.4.15-indy.jar:/Users/jbristow/.gradle/caches/modules-2/files-2.1/org.codehaus.groovy/groovy-sql/2.4.15/30111787e229a52c2e1cf38ba5976f172157ae5/groovy-sql-2.4.15-indy.jar:/Users/jbristow/.gradle/caches/modules-2/files-2.1/org.apache.commons/commons-lang3/3.3.1/6738a2da2202ce360f0af90aba005c1e05a2c4cd/commons-lang3-3.3.1.jar:/Users/jbristow/.gradle/caches/modules-2/files-2.1/io.github.microutils/kotlin-logging-common/1.6.22/b33a3ae8f364c1b425e6f730ca7cb35f21ae3a2/kotlin-logging-common-1.6.22.jar:/Users/jbristow/.gradle/caches/modules-2/files-2.1/org.apache.httpcomponents/httpcore/4.4.10/acc54d9b28bdffe4bbde89ed2e4a1e86b5285e2b/httpcore-4.4.10.jar:/Users/jbristow/.gradle/caches/modules-2/files-2.1/commons-logging/commons-logging/1.2/4bfc12adfe4842bf07b657f0369c4cb522955686/commons-logging-1.2.jar:/Users/jbristow/.gradle/caches/modules-2/files-2.1/commons-codec/commons-codec/1.10/4b95f4897fa13f2cd904aee711aeafc0c5295cd8/commons-codec-1.10.jar:/Users/jbristow/.gradle/caches/modules-2/files-2.1/org.jetbrains.kotlin/kotlin-stdlib-common/1.3.10/1b19d99229dcedad7caf50534dce38fe82845269/kotlin-stdlib-common-1.3.10.jar:/Users/jbristow/.gradle/caches/modules-2/files-2.1/org.jetbrains/annotations/13.0/919f0dfe192fb4e063e7dacadee7f8bb9a2672a9/annotations-13.0.jar:/Users/jbristow/.gradle/caches/modules-2/files-2.1/commons-lang/commons-lang/2.6/ce1edb914c94ebc388f086c6827e8bdeec71ac2/commons-lang-2.6.jar:/Users/jbristow/.gradle/caches/modules-2/files-2.1/com.jcabi/jcabi-log/0.14/819a57348f2448f01d74f8a317dab61d6a90cac2/jcabi-log-0.14.jar:/Users/jbristow/.gradle/caches/modules-2/files-2.1/org.reactivestreams/reactive-streams/1.0.1/1b1c911686eb40179219466e6a59b634b9d7a748/reactive-streams-1.0.1.jar Day23
answer1: 409
min: -177382187, -74661685, -49800667
max: 217323467, 155828966, 235151681
Point3d(x=38523060, y=42882252, z=69768757)


Drone is in range of n
(Drone(loc=Point3d(x=23721203, y=49916980, z=62688174), strength=69143159), 855), (Drone(loc=Point3d(x=25666789, y=46566129, z=59857685), strength=91298515), 852), (Drone(loc=Point3d(x=22021283, y=48754222, z=62532173), strength=78354445), 849), (Drone(loc=Point3d(x=22183273, y=47245435, z=61331039), strength=78148431), 842), (Drone(loc=Point3d(x=26105909, y=50897926, z=65297318), strength=72322969), 825)
n drones in range of
(Drone(loc=Point3d(x=28989030, y=55759287, z=54496492), strength=97599406), 921), (Drone(loc=Point3d(x=24489508, y=47708886, z=71646127), strength=96522070), 919), (Drone(loc=Point3d(x=25666789, y=46566129, z=59857685), strength=91298515), 915), (Drone(loc=Point3d(x=16334794, y=44582621, z=65327428), strength=96128412), 914), (Drone(loc=Point3d(x=35668794, y=51185049, z=66386584), strength=94793766), 897)

(Drone(loc=Point3d(x=61639792, y=155828966, z=114792617), strength=86493978), 1), (Drone(loc=Point3d(x=20387001, y=-49222476, z=158576211), strength=61669211), 1), (Drone(loc=Point3d(x=127775786, y=117371889, z=102292631), strength=61014901), 1), (Drone(loc=Point3d(x=32786493, y=39469143, z=235151681), strength=89274572), 1), (Drone(loc=Point3d(x=217323467, y=49856768, z=77188241), strength=62269637), 1)
(Drone(loc=Point3d(x=20387001, y=-49222476, z=158576211), strength=61669211), 1), (Drone(loc=Point3d(x=127775786, y=117371889, z=102292631), strength=61014901), 1), (Drone(loc=Point3d(x=170639226, y=7250757, z=71267258), strength=53751209), 1), (Drone(loc=Point3d(x=32786493, y=39469143, z=235151681), strength=89274572), 1), (Drone(loc=Point3d(x=217323467, y=49856768, z=77188241), strength=62269637), 1)
answer2: fart

Process finished with exit code 0


     */


    /*

    if isinstance(a, Probe) or isinstance(b, Tactic) or isinstance(c, Tactic):
    return Cond(a, b, c, ctx)
else:
    ctx = _get_ctx(_ctx_from_ast_arg_list([a, b, c], ctx))
    s = BoolSort(ctx)
    a = s.cast(a)
    b, c = _coerce_exprs(b, c, ctx)
    if __debug__:
        _z3_assert(a.ctx == b.ctx, "Context mismatch")
    return _to_expr_ref(Z3_mk_ite(ctx.ref(), a.as_ast(), b.as_ast(), c.as_ast()), ctx)

 */


    private fun Context.zabs(x: ArithExpr): ArithExpr {
        return (mkITE(
            mkGe(x, mkInt(0)),
            x,
            mkMul(x, mkInt(-1))
        ) as ArithExpr)
    }

    fun answer2(drones: Sequence<Drone>): Any {

        Context().use { ctx ->
            ctx.apply {

                val opt = ctx.mkOptimize()
                val x = ctx.mkIntConst("x")
                val y = ctx.mkIntConst("y")
                val z = ctx.mkIntConst("z")


                val rangeCount = ctx.mkIntConst("sum")
                val inRanges =
                    (0 until drones.count()).map {
                        mkIntConst("in_range_$it")
                    }.toTypedArray()

                drones.forEachIndexed { i, it ->
                    val (nx, ny, nz) = it.loc
                    val nrng = it.strength

                    opt.Add(
                        ctx.mkEq(
                            inRanges[i], mkITE(
                                mkLe(
                                    mkAdd(
                                        zabs(mkSub(x, mkInt(nx))),
                                        zabs(mkSub(y, mkInt(ny))),
                                        zabs(mkSub(z, mkInt(nz)))
                                    ),
                                    mkInt(nrng)
                                ),
                                ctx.mkInt(1),
                                ctx.mkInt(0)

                            )
                        )

                    )

                }
                opt.Add(mkEq(rangeCount, mkAdd(*inRanges)))
                val distFromZero = mkIntConst("dist")


                opt.Add(mkEq(distFromZero, mkAdd(zabs(x), zabs(y), zabs(z))))
                val h1 = opt.MkMaximize(rangeCount)
                val h2 = opt.MkMinimize(distFromZero)
                println(opt.Check())
                println("h1: ${h1.lower} ${h1.upper}")
                println("""h2: ${h2.lower}, ${h2.upper}""")
                println(opt.model.getConstInterp(x))
                println(opt.model.getConstInterp(y))
                println(opt.model.getConstInterp(z))
                val p = Point3d(
                    opt.model.getConstInterp(x).toString().toLong(),
                    opt.model.getConstInterp(y).toString().toLong(),
                    opt.model.getConstInterp(z).toString().toLong()
                )

                println(
                    "$p ${drones.count { it.inRange(p) }} ${p.manhattanDistance(
                        Point3d(0, 0, 0)
                    )}"
                )

            }
        }
        return "fart"
    }


    @Suppress("unused")
    val inputB = """pos=<10,12,12>, r=2
pos=<12,14,12>, r=2
pos=<16,12,12>, r=4
pos=<14,14,14>, r=6
pos=<50,50,50>, r=200
pos=<10,10,10>, r=5""".lines()

    @JvmStatic
    fun main(args: Array<String>) {
        val input =
            Files.readAllLines(Paths.get("src/main/resources/day23.txt"))

        val drones = input.asSequence().map {
            val xs =
                re.matchEntire(it)!!.groupValues.drop(1).map(String::toLong)
            Drone(xs[0], xs[1], xs[2], xs[3])
        }
        println("answer1: ${answer1(drones)}")
        println("answer2: ${answer2(drones)}")
    }
}





