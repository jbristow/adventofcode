package antlr.part1

import org.antlr.v4.runtime.tree.ParseTreeListener

/**
 * This interface defines a complete listener for a parse tree produced by
 * [Aoc2020Part1Parser].
 */
interface Aoc2020Listener : ParseTreeListener {
    /**
     * Enter a parse tree produced by [Aoc2020Part1Parser.start].
     * @param ctx the parse tree
     */
    fun enterStart(ctx: Aoc2020Part1Parser.StartContext)

    /**
     * Exit a parse tree produced by [Aoc2020Part1Parser.start].
     * @param ctx the parse tree
     */
    fun exitStart(ctx: Aoc2020Part1Parser.StartContext)

    /**
     * Enter a parse tree produced by [Aoc2020Part1Parser.r65].
     * @param ctx the parse tree
     */
    fun enterR65(ctx: Aoc2020Part1Parser.R65Context?)

    /**
     * Exit a parse tree produced by [Aoc2020Part1Parser.r65].
     * @param ctx the parse tree
     */
    fun exitR65(ctx: Aoc2020Part1Parser.R65Context?)

    /**
     * Enter a parse tree produced by [Aoc2020Part1Parser.r35].
     * @param ctx the parse tree
     */
    fun enterR35(ctx: Aoc2020Part1Parser.R35Context?)

    /**
     * Exit a parse tree produced by [Aoc2020Part1Parser.r35].
     * @param ctx the parse tree
     */
    fun exitR35(ctx: Aoc2020Part1Parser.R35Context?)

    /**
     * Enter a parse tree produced by [Aoc2020Part1Parser.r28].
     * @param ctx the parse tree
     */
    fun enterR28(ctx: Aoc2020Part1Parser.R28Context?)

    /**
     * Exit a parse tree produced by [Aoc2020Part1Parser.r28].
     * @param ctx the parse tree
     */
    fun exitR28(ctx: Aoc2020Part1Parser.R28Context?)

    /**
     * Enter a parse tree produced by [Aoc2020Part1Parser.r81].
     * @param ctx the parse tree
     */
    fun enterR81(ctx: Aoc2020Part1Parser.R81Context?)

    /**
     * Exit a parse tree produced by [Aoc2020Part1Parser.r81].
     * @param ctx the parse tree
     */
    fun exitR81(ctx: Aoc2020Part1Parser.R81Context?)

    /**
     * Enter a parse tree produced by [Aoc2020Part1Parser.r100].
     * @param ctx the parse tree
     */
    fun enterR100(ctx: Aoc2020Part1Parser.R100Context?)

    /**
     * Exit a parse tree produced by [Aoc2020Part1Parser.r100].
     * @param ctx the parse tree
     */
    fun exitR100(ctx: Aoc2020Part1Parser.R100Context?)

    /**
     * Enter a parse tree produced by [Aoc2020Part1Parser.r4].
     * @param ctx the parse tree
     */
    fun enterR4(ctx: Aoc2020Part1Parser.R4Context?)

    /**
     * Exit a parse tree produced by [Aoc2020Part1Parser.r4].
     * @param ctx the parse tree
     */
    fun exitR4(ctx: Aoc2020Part1Parser.R4Context?)

    /**
     * Enter a parse tree produced by [Aoc2020Part1Parser.r93].
     * @param ctx the parse tree
     */
    fun enterR93(ctx: Aoc2020Part1Parser.R93Context?)

    /**
     * Exit a parse tree produced by [Aoc2020Part1Parser.r93].
     * @param ctx the parse tree
     */
    fun exitR93(ctx: Aoc2020Part1Parser.R93Context?)

    /**
     * Enter a parse tree produced by [Aoc2020Part1Parser.r60].
     * @param ctx the parse tree
     */
    fun enterR60(ctx: Aoc2020Part1Parser.R60Context?)

    /**
     * Exit a parse tree produced by [Aoc2020Part1Parser.r60].
     * @param ctx the parse tree
     */
    fun exitR60(ctx: Aoc2020Part1Parser.R60Context?)

    /**
     * Enter a parse tree produced by [Aoc2020Part1Parser.r19].
     * @param ctx the parse tree
     */
    fun enterR19(ctx: Aoc2020Part1Parser.R19Context?)

    /**
     * Exit a parse tree produced by [Aoc2020Part1Parser.r19].
     * @param ctx the parse tree
     */
    fun exitR19(ctx: Aoc2020Part1Parser.R19Context?)

    /**
     * Enter a parse tree produced by [Aoc2020Part1Parser.r110].
     * @param ctx the parse tree
     */
    fun enterR110(ctx: Aoc2020Part1Parser.R110Context?)

    /**
     * Exit a parse tree produced by [Aoc2020Part1Parser.r110].
     * @param ctx the parse tree
     */
    fun exitR110(ctx: Aoc2020Part1Parser.R110Context?)

    /**
     * Enter a parse tree produced by [Aoc2020Part1Parser.r116].
     * @param ctx the parse tree
     */
    fun enterR116(ctx: Aoc2020Part1Parser.R116Context?)

    /**
     * Exit a parse tree produced by [Aoc2020Part1Parser.r116].
     * @param ctx the parse tree
     */
    fun exitR116(ctx: Aoc2020Part1Parser.R116Context?)

    /**
     * Enter a parse tree produced by [Aoc2020Part1Parser.r48].
     * @param ctx the parse tree
     */
    fun enterR48(ctx: Aoc2020Part1Parser.R48Context?)

    /**
     * Exit a parse tree produced by [Aoc2020Part1Parser.r48].
     * @param ctx the parse tree
     */
    fun exitR48(ctx: Aoc2020Part1Parser.R48Context?)

    /**
     * Enter a parse tree produced by [Aoc2020Part1Parser.r82].
     * @param ctx the parse tree
     */
    fun enterR82(ctx: Aoc2020Part1Parser.R82Context?)

    /**
     * Exit a parse tree produced by [Aoc2020Part1Parser.r82].
     * @param ctx the parse tree
     */
    fun exitR82(ctx: Aoc2020Part1Parser.R82Context?)

    /**
     * Enter a parse tree produced by [Aoc2020Part1Parser.r26].
     * @param ctx the parse tree
     */
    fun enterR26(ctx: Aoc2020Part1Parser.R26Context?)

    /**
     * Exit a parse tree produced by [Aoc2020Part1Parser.r26].
     * @param ctx the parse tree
     */
    fun exitR26(ctx: Aoc2020Part1Parser.R26Context?)

    /**
     * Enter a parse tree produced by [Aoc2020Part1Parser.r89].
     * @param ctx the parse tree
     */
    fun enterR89(ctx: Aoc2020Part1Parser.R89Context?)

    /**
     * Exit a parse tree produced by [Aoc2020Part1Parser.r89].
     * @param ctx the parse tree
     */
    fun exitR89(ctx: Aoc2020Part1Parser.R89Context?)

    /**
     * Enter a parse tree produced by [Aoc2020Part1Parser.r47].
     * @param ctx the parse tree
     */
    fun enterR47(ctx: Aoc2020Part1Parser.R47Context?)

    /**
     * Exit a parse tree produced by [Aoc2020Part1Parser.r47].
     * @param ctx the parse tree
     */
    fun exitR47(ctx: Aoc2020Part1Parser.R47Context?)

    /**
     * Enter a parse tree produced by [Aoc2020Part1Parser.r56].
     * @param ctx the parse tree
     */
    fun enterR56(ctx: Aoc2020Part1Parser.R56Context?)

    /**
     * Exit a parse tree produced by [Aoc2020Part1Parser.r56].
     * @param ctx the parse tree
     */
    fun exitR56(ctx: Aoc2020Part1Parser.R56Context?)

    /**
     * Enter a parse tree produced by [Aoc2020Part1Parser.r90].
     * @param ctx the parse tree
     */
    fun enterR90(ctx: Aoc2020Part1Parser.R90Context?)

    /**
     * Exit a parse tree produced by [Aoc2020Part1Parser.r90].
     * @param ctx the parse tree
     */
    fun exitR90(ctx: Aoc2020Part1Parser.R90Context?)

    /**
     * Enter a parse tree produced by [Aoc2020Part1Parser.r43].
     * @param ctx the parse tree
     */
    fun enterR43(ctx: Aoc2020Part1Parser.R43Context?)

    /**
     * Exit a parse tree produced by [Aoc2020Part1Parser.r43].
     * @param ctx the parse tree
     */
    fun exitR43(ctx: Aoc2020Part1Parser.R43Context?)

    /**
     * Enter a parse tree produced by [Aoc2020Part1Parser.r94].
     * @param ctx the parse tree
     */
    fun enterR94(ctx: Aoc2020Part1Parser.R94Context?)

    /**
     * Exit a parse tree produced by [Aoc2020Part1Parser.r94].
     * @param ctx the parse tree
     */
    fun exitR94(ctx: Aoc2020Part1Parser.R94Context?)

    /**
     * Enter a parse tree produced by [Aoc2020Part1Parser.r70].
     * @param ctx the parse tree
     */
    fun enterR70(ctx: Aoc2020Part1Parser.R70Context?)

    /**
     * Exit a parse tree produced by [Aoc2020Part1Parser.r70].
     * @param ctx the parse tree
     */
    fun exitR70(ctx: Aoc2020Part1Parser.R70Context?)

    /**
     * Enter a parse tree produced by [Aoc2020Part1Parser.r55].
     * @param ctx the parse tree
     */
    fun enterR55(ctx: Aoc2020Part1Parser.R55Context?)

    /**
     * Exit a parse tree produced by [Aoc2020Part1Parser.r55].
     * @param ctx the parse tree
     */
    fun exitR55(ctx: Aoc2020Part1Parser.R55Context?)

    /**
     * Enter a parse tree produced by [Aoc2020Part1Parser.r84].
     * @param ctx the parse tree
     */
    fun enterR84(ctx: Aoc2020Part1Parser.R84Context?)

    /**
     * Exit a parse tree produced by [Aoc2020Part1Parser.r84].
     * @param ctx the parse tree
     */
    fun exitR84(ctx: Aoc2020Part1Parser.R84Context?)

    /**
     * Enter a parse tree produced by [Aoc2020Part1Parser.r128].
     * @param ctx the parse tree
     */
    fun enterR128(ctx: Aoc2020Part1Parser.R128Context?)

    /**
     * Exit a parse tree produced by [Aoc2020Part1Parser.r128].
     * @param ctx the parse tree
     */
    fun exitR128(ctx: Aoc2020Part1Parser.R128Context?)

    /**
     * Enter a parse tree produced by [Aoc2020Part1Parser.r34].
     * @param ctx the parse tree
     */
    fun enterR34(ctx: Aoc2020Part1Parser.R34Context?)

    /**
     * Exit a parse tree produced by [Aoc2020Part1Parser.r34].
     * @param ctx the parse tree
     */
    fun exitR34(ctx: Aoc2020Part1Parser.R34Context?)

    /**
     * Enter a parse tree produced by [Aoc2020Part1Parser.r105].
     * @param ctx the parse tree
     */
    fun enterR105(ctx: Aoc2020Part1Parser.R105Context?)

    /**
     * Exit a parse tree produced by [Aoc2020Part1Parser.r105].
     * @param ctx the parse tree
     */
    fun exitR105(ctx: Aoc2020Part1Parser.R105Context?)

    /**
     * Enter a parse tree produced by [Aoc2020Part1Parser.r126].
     * @param ctx the parse tree
     */
    fun enterR126(ctx: Aoc2020Part1Parser.R126Context?)

    /**
     * Exit a parse tree produced by [Aoc2020Part1Parser.r126].
     * @param ctx the parse tree
     */
    fun exitR126(ctx: Aoc2020Part1Parser.R126Context?)

    /**
     * Enter a parse tree produced by [Aoc2020Part1Parser.r107].
     * @param ctx the parse tree
     */
    fun enterR107(ctx: Aoc2020Part1Parser.R107Context?)

    /**
     * Exit a parse tree produced by [Aoc2020Part1Parser.r107].
     * @param ctx the parse tree
     */
    fun exitR107(ctx: Aoc2020Part1Parser.R107Context?)

    /**
     * Enter a parse tree produced by [Aoc2020Part1Parser.r63].
     * @param ctx the parse tree
     */
    fun enterR63(ctx: Aoc2020Part1Parser.R63Context?)

    /**
     * Exit a parse tree produced by [Aoc2020Part1Parser.r63].
     * @param ctx the parse tree
     */
    fun exitR63(ctx: Aoc2020Part1Parser.R63Context?)

    /**
     * Enter a parse tree produced by [Aoc2020Part1Parser.r27].
     * @param ctx the parse tree
     */
    fun enterR27(ctx: Aoc2020Part1Parser.R27Context?)

    /**
     * Exit a parse tree produced by [Aoc2020Part1Parser.r27].
     * @param ctx the parse tree
     */
    fun exitR27(ctx: Aoc2020Part1Parser.R27Context?)

    /**
     * Enter a parse tree produced by [Aoc2020Part1Parser.r106].
     * @param ctx the parse tree
     */
    fun enterR106(ctx: Aoc2020Part1Parser.R106Context?)

    /**
     * Exit a parse tree produced by [Aoc2020Part1Parser.r106].
     * @param ctx the parse tree
     */
    fun exitR106(ctx: Aoc2020Part1Parser.R106Context?)

    /**
     * Enter a parse tree produced by [Aoc2020Part1Parser.r132].
     * @param ctx the parse tree
     */
    fun enterR132(ctx: Aoc2020Part1Parser.R132Context?)

    /**
     * Exit a parse tree produced by [Aoc2020Part1Parser.r132].
     * @param ctx the parse tree
     */
    fun exitR132(ctx: Aoc2020Part1Parser.R132Context?)

    /**
     * Enter a parse tree produced by [Aoc2020Part1Parser.r5].
     * @param ctx the parse tree
     */
    fun enterR5(ctx: Aoc2020Part1Parser.R5Context?)

    /**
     * Exit a parse tree produced by [Aoc2020Part1Parser.r5].
     * @param ctx the parse tree
     */
    fun exitR5(ctx: Aoc2020Part1Parser.R5Context?)

    /**
     * Enter a parse tree produced by [Aoc2020Part1Parser.r91].
     * @param ctx the parse tree
     */
    fun enterR91(ctx: Aoc2020Part1Parser.R91Context?)

    /**
     * Exit a parse tree produced by [Aoc2020Part1Parser.r91].
     * @param ctx the parse tree
     */
    fun exitR91(ctx: Aoc2020Part1Parser.R91Context?)

    /**
     * Enter a parse tree produced by [Aoc2020Part1Parser.r52].
     * @param ctx the parse tree
     */
    fun enterR52(ctx: Aoc2020Part1Parser.R52Context?)

    /**
     * Exit a parse tree produced by [Aoc2020Part1Parser.r52].
     * @param ctx the parse tree
     */
    fun exitR52(ctx: Aoc2020Part1Parser.R52Context?)

    /**
     * Enter a parse tree produced by [Aoc2020Part1Parser.r37].
     * @param ctx the parse tree
     */
    fun enterR37(ctx: Aoc2020Part1Parser.R37Context?)

    /**
     * Exit a parse tree produced by [Aoc2020Part1Parser.r37].
     * @param ctx the parse tree
     */
    fun exitR37(ctx: Aoc2020Part1Parser.R37Context?)

    /**
     * Enter a parse tree produced by [Aoc2020Part1Parser.r30].
     * @param ctx the parse tree
     */
    fun enterR30(ctx: Aoc2020Part1Parser.R30Context?)

    /**
     * Exit a parse tree produced by [Aoc2020Part1Parser.r30].
     * @param ctx the parse tree
     */
    fun exitR30(ctx: Aoc2020Part1Parser.R30Context?)

    /**
     * Enter a parse tree produced by [Aoc2020Part1Parser.r73].
     * @param ctx the parse tree
     */
    fun enterR73(ctx: Aoc2020Part1Parser.R73Context?)

    /**
     * Exit a parse tree produced by [Aoc2020Part1Parser.r73].
     * @param ctx the parse tree
     */
    fun exitR73(ctx: Aoc2020Part1Parser.R73Context?)

    /**
     * Enter a parse tree produced by [Aoc2020Part1Parser.r130].
     * @param ctx the parse tree
     */
    fun enterR130(ctx: Aoc2020Part1Parser.R130Context?)

    /**
     * Exit a parse tree produced by [Aoc2020Part1Parser.r130].
     * @param ctx the parse tree
     */
    fun exitR130(ctx: Aoc2020Part1Parser.R130Context?)

    /**
     * Enter a parse tree produced by [Aoc2020Part1Parser.r129].
     * @param ctx the parse tree
     */
    fun enterR129(ctx: Aoc2020Part1Parser.R129Context?)

    /**
     * Exit a parse tree produced by [Aoc2020Part1Parser.r129].
     * @param ctx the parse tree
     */
    fun exitR129(ctx: Aoc2020Part1Parser.R129Context?)

    /**
     * Enter a parse tree produced by [Aoc2020Part1Parser.r85].
     * @param ctx the parse tree
     */
    fun enterR85(ctx: Aoc2020Part1Parser.R85Context?)

    /**
     * Exit a parse tree produced by [Aoc2020Part1Parser.r85].
     * @param ctx the parse tree
     */
    fun exitR85(ctx: Aoc2020Part1Parser.R85Context?)

    /**
     * Enter a parse tree produced by [Aoc2020Part1Parser.r29].
     * @param ctx the parse tree
     */
    fun enterR29(ctx: Aoc2020Part1Parser.R29Context?)

    /**
     * Exit a parse tree produced by [Aoc2020Part1Parser.r29].
     * @param ctx the parse tree
     */
    fun exitR29(ctx: Aoc2020Part1Parser.R29Context?)

    /**
     * Enter a parse tree produced by [Aoc2020Part1Parser.r96].
     * @param ctx the parse tree
     */
    fun enterR96(ctx: Aoc2020Part1Parser.R96Context?)

    /**
     * Exit a parse tree produced by [Aoc2020Part1Parser.r96].
     * @param ctx the parse tree
     */
    fun exitR96(ctx: Aoc2020Part1Parser.R96Context?)

    /**
     * Enter a parse tree produced by [Aoc2020Part1Parser.r75].
     * @param ctx the parse tree
     */
    fun enterR75(ctx: Aoc2020Part1Parser.R75Context?)

    /**
     * Exit a parse tree produced by [Aoc2020Part1Parser.r75].
     * @param ctx the parse tree
     */
    fun exitR75(ctx: Aoc2020Part1Parser.R75Context?)

    /**
     * Enter a parse tree produced by [Aoc2020Part1Parser.r58].
     * @param ctx the parse tree
     */
    fun enterR58(ctx: Aoc2020Part1Parser.R58Context?)

    /**
     * Exit a parse tree produced by [Aoc2020Part1Parser.r58].
     * @param ctx the parse tree
     */
    fun exitR58(ctx: Aoc2020Part1Parser.R58Context?)

    /**
     * Enter a parse tree produced by [Aoc2020Part1Parser.r11].
     * @param ctx the parse tree
     */
    fun enterR11(ctx: Aoc2020Part1Parser.R11Context?)

    /**
     * Exit a parse tree produced by [Aoc2020Part1Parser.r11].
     * @param ctx the parse tree
     */
    fun exitR11(ctx: Aoc2020Part1Parser.R11Context?)

    /**
     * Enter a parse tree produced by [Aoc2020Part1Parser.r7].
     * @param ctx the parse tree
     */
    fun enterR7(ctx: Aoc2020Part1Parser.R7Context?)

    /**
     * Exit a parse tree produced by [Aoc2020Part1Parser.r7].
     * @param ctx the parse tree
     */
    fun exitR7(ctx: Aoc2020Part1Parser.R7Context?)

    /**
     * Enter a parse tree produced by [Aoc2020Part1Parser.r59].
     * @param ctx the parse tree
     */
    fun enterR59(ctx: Aoc2020Part1Parser.R59Context?)

    /**
     * Exit a parse tree produced by [Aoc2020Part1Parser.r59].
     * @param ctx the parse tree
     */
    fun exitR59(ctx: Aoc2020Part1Parser.R59Context?)

    /**
     * Enter a parse tree produced by [Aoc2020Part1Parser.r118].
     * @param ctx the parse tree
     */
    fun enterR118(ctx: Aoc2020Part1Parser.R118Context?)

    /**
     * Exit a parse tree produced by [Aoc2020Part1Parser.r118].
     * @param ctx the parse tree
     */
    fun exitR118(ctx: Aoc2020Part1Parser.R118Context?)

    /**
     * Enter a parse tree produced by [Aoc2020Part1Parser.r33].
     * @param ctx the parse tree
     */
    fun enterR33(ctx: Aoc2020Part1Parser.R33Context?)

    /**
     * Exit a parse tree produced by [Aoc2020Part1Parser.r33].
     * @param ctx the parse tree
     */
    fun exitR33(ctx: Aoc2020Part1Parser.R33Context?)

    /**
     * Enter a parse tree produced by [Aoc2020Part1Parser.r16].
     * @param ctx the parse tree
     */
    fun enterR16(ctx: Aoc2020Part1Parser.R16Context?)

    /**
     * Exit a parse tree produced by [Aoc2020Part1Parser.r16].
     * @param ctx the parse tree
     */
    fun exitR16(ctx: Aoc2020Part1Parser.R16Context?)

    /**
     * Enter a parse tree produced by [Aoc2020Part1Parser.r92].
     * @param ctx the parse tree
     */
    fun enterR92(ctx: Aoc2020Part1Parser.R92Context?)

    /**
     * Exit a parse tree produced by [Aoc2020Part1Parser.r92].
     * @param ctx the parse tree
     */
    fun exitR92(ctx: Aoc2020Part1Parser.R92Context?)

    /**
     * Enter a parse tree produced by [Aoc2020Part1Parser.r61].
     * @param ctx the parse tree
     */
    fun enterR61(ctx: Aoc2020Part1Parser.R61Context?)

    /**
     * Exit a parse tree produced by [Aoc2020Part1Parser.r61].
     * @param ctx the parse tree
     */
    fun exitR61(ctx: Aoc2020Part1Parser.R61Context?)

    /**
     * Enter a parse tree produced by [Aoc2020Part1Parser.r111].
     * @param ctx the parse tree
     */
    fun enterR111(ctx: Aoc2020Part1Parser.R111Context?)

    /**
     * Exit a parse tree produced by [Aoc2020Part1Parser.r111].
     * @param ctx the parse tree
     */
    fun exitR111(ctx: Aoc2020Part1Parser.R111Context?)

    /**
     * Enter a parse tree produced by [Aoc2020Part1Parser.r88].
     * @param ctx the parse tree
     */
    fun enterR88(ctx: Aoc2020Part1Parser.R88Context?)

    /**
     * Exit a parse tree produced by [Aoc2020Part1Parser.r88].
     * @param ctx the parse tree
     */
    fun exitR88(ctx: Aoc2020Part1Parser.R88Context?)

    /**
     * Enter a parse tree produced by [Aoc2020Part1Parser.r114].
     * @param ctx the parse tree
     */
    fun enterR114(ctx: Aoc2020Part1Parser.R114Context?)

    /**
     * Exit a parse tree produced by [Aoc2020Part1Parser.r114].
     * @param ctx the parse tree
     */
    fun exitR114(ctx: Aoc2020Part1Parser.R114Context?)

    /**
     * Enter a parse tree produced by [Aoc2020Part1Parser.r103].
     * @param ctx the parse tree
     */
    fun enterR103(ctx: Aoc2020Part1Parser.R103Context?)

    /**
     * Exit a parse tree produced by [Aoc2020Part1Parser.r103].
     * @param ctx the parse tree
     */
    fun exitR103(ctx: Aoc2020Part1Parser.R103Context?)

    /**
     * Enter a parse tree produced by [Aoc2020Part1Parser.r38].
     * @param ctx the parse tree
     */
    fun enterR38(ctx: Aoc2020Part1Parser.R38Context?)

    /**
     * Exit a parse tree produced by [Aoc2020Part1Parser.r38].
     * @param ctx the parse tree
     */
    fun exitR38(ctx: Aoc2020Part1Parser.R38Context?)

    /**
     * Enter a parse tree produced by [Aoc2020Part1Parser.r44].
     * @param ctx the parse tree
     */
    fun enterR44(ctx: Aoc2020Part1Parser.R44Context?)

    /**
     * Exit a parse tree produced by [Aoc2020Part1Parser.r44].
     * @param ctx the parse tree
     */
    fun exitR44(ctx: Aoc2020Part1Parser.R44Context?)

    /**
     * Enter a parse tree produced by [Aoc2020Part1Parser.r76].
     * @param ctx the parse tree
     */
    fun enterR76(ctx: Aoc2020Part1Parser.R76Context?)

    /**
     * Exit a parse tree produced by [Aoc2020Part1Parser.r76].
     * @param ctx the parse tree
     */
    fun exitR76(ctx: Aoc2020Part1Parser.R76Context?)

    /**
     * Enter a parse tree produced by [Aoc2020Part1Parser.r68].
     * @param ctx the parse tree
     */
    fun enterR68(ctx: Aoc2020Part1Parser.R68Context?)

    /**
     * Exit a parse tree produced by [Aoc2020Part1Parser.r68].
     * @param ctx the parse tree
     */
    fun exitR68(ctx: Aoc2020Part1Parser.R68Context?)

    /**
     * Enter a parse tree produced by [Aoc2020Part1Parser.r22].
     * @param ctx the parse tree
     */
    fun enterR22(ctx: Aoc2020Part1Parser.R22Context?)

    /**
     * Exit a parse tree produced by [Aoc2020Part1Parser.r22].
     * @param ctx the parse tree
     */
    fun exitR22(ctx: Aoc2020Part1Parser.R22Context?)

    /**
     * Enter a parse tree produced by [Aoc2020Part1Parser.r8].
     * @param ctx the parse tree
     */
    fun enterR8(ctx: Aoc2020Part1Parser.R8Context?)

    /**
     * Exit a parse tree produced by [Aoc2020Part1Parser.r8].
     * @param ctx the parse tree
     */
    fun exitR8(ctx: Aoc2020Part1Parser.R8Context?)

    /**
     * Enter a parse tree produced by [Aoc2020Part1Parser.r113].
     * @param ctx the parse tree
     */
    fun enterR113(ctx: Aoc2020Part1Parser.R113Context?)

    /**
     * Exit a parse tree produced by [Aoc2020Part1Parser.r113].
     * @param ctx the parse tree
     */
    fun exitR113(ctx: Aoc2020Part1Parser.R113Context?)

    /**
     * Enter a parse tree produced by [Aoc2020Part1Parser.r124].
     * @param ctx the parse tree
     */
    fun enterR124(ctx: Aoc2020Part1Parser.R124Context?)

    /**
     * Exit a parse tree produced by [Aoc2020Part1Parser.r124].
     * @param ctx the parse tree
     */
    fun exitR124(ctx: Aoc2020Part1Parser.R124Context?)

    /**
     * Enter a parse tree produced by [Aoc2020Part1Parser.r104].
     * @param ctx the parse tree
     */
    fun enterR104(ctx: Aoc2020Part1Parser.R104Context?)

    /**
     * Exit a parse tree produced by [Aoc2020Part1Parser.r104].
     * @param ctx the parse tree
     */
    fun exitR104(ctx: Aoc2020Part1Parser.R104Context?)

    /**
     * Enter a parse tree produced by [Aoc2020Part1Parser.r20].
     * @param ctx the parse tree
     */
    fun enterR20(ctx: Aoc2020Part1Parser.R20Context?)

    /**
     * Exit a parse tree produced by [Aoc2020Part1Parser.r20].
     * @param ctx the parse tree
     */
    fun exitR20(ctx: Aoc2020Part1Parser.R20Context?)

    /**
     * Enter a parse tree produced by [Aoc2020Part1Parser.r49].
     * @param ctx the parse tree
     */
    fun enterR49(ctx: Aoc2020Part1Parser.R49Context?)

    /**
     * Exit a parse tree produced by [Aoc2020Part1Parser.r49].
     * @param ctx the parse tree
     */
    fun exitR49(ctx: Aoc2020Part1Parser.R49Context?)

    /**
     * Enter a parse tree produced by [Aoc2020Part1Parser.r21].
     * @param ctx the parse tree
     */
    fun enterR21(ctx: Aoc2020Part1Parser.R21Context?)

    /**
     * Exit a parse tree produced by [Aoc2020Part1Parser.r21].
     * @param ctx the parse tree
     */
    fun exitR21(ctx: Aoc2020Part1Parser.R21Context?)

    /**
     * Enter a parse tree produced by [Aoc2020Part1Parser.r109].
     * @param ctx the parse tree
     */
    fun enterR109(ctx: Aoc2020Part1Parser.R109Context?)

    /**
     * Exit a parse tree produced by [Aoc2020Part1Parser.r109].
     * @param ctx the parse tree
     */
    fun exitR109(ctx: Aoc2020Part1Parser.R109Context?)

    /**
     * Enter a parse tree produced by [Aoc2020Part1Parser.r1].
     * @param ctx the parse tree
     */
    fun enterR1(ctx: Aoc2020Part1Parser.R1Context?)

    /**
     * Exit a parse tree produced by [Aoc2020Part1Parser.r1].
     * @param ctx the parse tree
     */
    fun exitR1(ctx: Aoc2020Part1Parser.R1Context?)

    /**
     * Enter a parse tree produced by [Aoc2020Part1Parser.r121].
     * @param ctx the parse tree
     */
    fun enterR121(ctx: Aoc2020Part1Parser.R121Context?)

    /**
     * Exit a parse tree produced by [Aoc2020Part1Parser.r121].
     * @param ctx the parse tree
     */
    fun exitR121(ctx: Aoc2020Part1Parser.R121Context?)

    /**
     * Enter a parse tree produced by [Aoc2020Part1Parser.r99].
     * @param ctx the parse tree
     */
    fun enterR99(ctx: Aoc2020Part1Parser.R99Context?)

    /**
     * Exit a parse tree produced by [Aoc2020Part1Parser.r99].
     * @param ctx the parse tree
     */
    fun exitR99(ctx: Aoc2020Part1Parser.R99Context?)

    /**
     * Enter a parse tree produced by [Aoc2020Part1Parser.r42].
     * @param ctx the parse tree
     */
    fun enterR42(ctx: Aoc2020Part1Parser.R42Context?)

    /**
     * Exit a parse tree produced by [Aoc2020Part1Parser.r42].
     * @param ctx the parse tree
     */
    fun exitR42(ctx: Aoc2020Part1Parser.R42Context?)

    /**
     * Enter a parse tree produced by [Aoc2020Part1Parser.r64].
     * @param ctx the parse tree
     */
    fun enterR64(ctx: Aoc2020Part1Parser.R64Context?)

    /**
     * Exit a parse tree produced by [Aoc2020Part1Parser.r64].
     * @param ctx the parse tree
     */
    fun exitR64(ctx: Aoc2020Part1Parser.R64Context?)

    /**
     * Enter a parse tree produced by [Aoc2020Part1Parser.r102].
     * @param ctx the parse tree
     */
    fun enterR102(ctx: Aoc2020Part1Parser.R102Context?)

    /**
     * Exit a parse tree produced by [Aoc2020Part1Parser.r102].
     * @param ctx the parse tree
     */
    fun exitR102(ctx: Aoc2020Part1Parser.R102Context?)

    /**
     * Enter a parse tree produced by [Aoc2020Part1Parser.r62].
     * @param ctx the parse tree
     */
    fun enterR62(ctx: Aoc2020Part1Parser.R62Context?)

    /**
     * Exit a parse tree produced by [Aoc2020Part1Parser.r62].
     * @param ctx the parse tree
     */
    fun exitR62(ctx: Aoc2020Part1Parser.R62Context?)

    /**
     * Enter a parse tree produced by [Aoc2020Part1Parser.r50].
     * @param ctx the parse tree
     */
    fun enterR50(ctx: Aoc2020Part1Parser.R50Context?)

    /**
     * Exit a parse tree produced by [Aoc2020Part1Parser.r50].
     * @param ctx the parse tree
     */
    fun exitR50(ctx: Aoc2020Part1Parser.R50Context?)

    /**
     * Enter a parse tree produced by [Aoc2020Part1Parser.r67].
     * @param ctx the parse tree
     */
    fun enterR67(ctx: Aoc2020Part1Parser.R67Context?)

    /**
     * Exit a parse tree produced by [Aoc2020Part1Parser.r67].
     * @param ctx the parse tree
     */
    fun exitR67(ctx: Aoc2020Part1Parser.R67Context?)

    /**
     * Enter a parse tree produced by [Aoc2020Part1Parser.r83].
     * @param ctx the parse tree
     */
    fun enterR83(ctx: Aoc2020Part1Parser.R83Context?)

    /**
     * Exit a parse tree produced by [Aoc2020Part1Parser.r83].
     * @param ctx the parse tree
     */
    fun exitR83(ctx: Aoc2020Part1Parser.R83Context?)

    /**
     * Enter a parse tree produced by [Aoc2020Part1Parser.r25].
     * @param ctx the parse tree
     */
    fun enterR25(ctx: Aoc2020Part1Parser.R25Context?)

    /**
     * Exit a parse tree produced by [Aoc2020Part1Parser.r25].
     * @param ctx the parse tree
     */
    fun exitR25(ctx: Aoc2020Part1Parser.R25Context?)

    /**
     * Enter a parse tree produced by [Aoc2020Part1Parser.r97].
     * @param ctx the parse tree
     */
    fun enterR97(ctx: Aoc2020Part1Parser.R97Context?)

    /**
     * Exit a parse tree produced by [Aoc2020Part1Parser.r97].
     * @param ctx the parse tree
     */
    fun exitR97(ctx: Aoc2020Part1Parser.R97Context?)

    /**
     * Enter a parse tree produced by [Aoc2020Part1Parser.r46].
     * @param ctx the parse tree
     */
    fun enterR46(ctx: Aoc2020Part1Parser.R46Context?)

    /**
     * Exit a parse tree produced by [Aoc2020Part1Parser.r46].
     * @param ctx the parse tree
     */
    fun exitR46(ctx: Aoc2020Part1Parser.R46Context?)

    /**
     * Enter a parse tree produced by [Aoc2020Part1Parser.r15].
     * @param ctx the parse tree
     */
    fun enterR15(ctx: Aoc2020Part1Parser.R15Context?)

    /**
     * Exit a parse tree produced by [Aoc2020Part1Parser.r15].
     * @param ctx the parse tree
     */
    fun exitR15(ctx: Aoc2020Part1Parser.R15Context?)

    /**
     * Enter a parse tree produced by [Aoc2020Part1Parser.r108].
     * @param ctx the parse tree
     */
    fun enterR108(ctx: Aoc2020Part1Parser.R108Context?)

    /**
     * Exit a parse tree produced by [Aoc2020Part1Parser.r108].
     * @param ctx the parse tree
     */
    fun exitR108(ctx: Aoc2020Part1Parser.R108Context?)

    /**
     * Enter a parse tree produced by [Aoc2020Part1Parser.r10].
     * @param ctx the parse tree
     */
    fun enterR10(ctx: Aoc2020Part1Parser.R10Context?)

    /**
     * Exit a parse tree produced by [Aoc2020Part1Parser.r10].
     * @param ctx the parse tree
     */
    fun exitR10(ctx: Aoc2020Part1Parser.R10Context?)

    /**
     * Enter a parse tree produced by [Aoc2020Part1Parser.r95].
     * @param ctx the parse tree
     */
    fun enterR95(ctx: Aoc2020Part1Parser.R95Context?)

    /**
     * Exit a parse tree produced by [Aoc2020Part1Parser.r95].
     * @param ctx the parse tree
     */
    fun exitR95(ctx: Aoc2020Part1Parser.R95Context?)

    /**
     * Enter a parse tree produced by [Aoc2020Part1Parser.r127].
     * @param ctx the parse tree
     */
    fun enterR127(ctx: Aoc2020Part1Parser.R127Context?)

    /**
     * Exit a parse tree produced by [Aoc2020Part1Parser.r127].
     * @param ctx the parse tree
     */
    fun exitR127(ctx: Aoc2020Part1Parser.R127Context?)

    /**
     * Enter a parse tree produced by [Aoc2020Part1Parser.r77].
     * @param ctx the parse tree
     */
    fun enterR77(ctx: Aoc2020Part1Parser.R77Context?)

    /**
     * Exit a parse tree produced by [Aoc2020Part1Parser.r77].
     * @param ctx the parse tree
     */
    fun exitR77(ctx: Aoc2020Part1Parser.R77Context?)

    /**
     * Enter a parse tree produced by [Aoc2020Part1Parser.r69].
     * @param ctx the parse tree
     */
    fun enterR69(ctx: Aoc2020Part1Parser.R69Context?)

    /**
     * Exit a parse tree produced by [Aoc2020Part1Parser.r69].
     * @param ctx the parse tree
     */
    fun exitR69(ctx: Aoc2020Part1Parser.R69Context?)

    /**
     * Enter a parse tree produced by [Aoc2020Part1Parser.r23].
     * @param ctx the parse tree
     */
    fun enterR23(ctx: Aoc2020Part1Parser.R23Context?)

    /**
     * Exit a parse tree produced by [Aoc2020Part1Parser.r23].
     * @param ctx the parse tree
     */
    fun exitR23(ctx: Aoc2020Part1Parser.R23Context?)

    /**
     * Enter a parse tree produced by [Aoc2020Part1Parser.r54].
     * @param ctx the parse tree
     */
    fun enterR54(ctx: Aoc2020Part1Parser.R54Context?)

    /**
     * Exit a parse tree produced by [Aoc2020Part1Parser.r54].
     * @param ctx the parse tree
     */
    fun exitR54(ctx: Aoc2020Part1Parser.R54Context?)

    /**
     * Enter a parse tree produced by [Aoc2020Part1Parser.r6].
     * @param ctx the parse tree
     */
    fun enterR6(ctx: Aoc2020Part1Parser.R6Context?)

    /**
     * Exit a parse tree produced by [Aoc2020Part1Parser.r6].
     * @param ctx the parse tree
     */
    fun exitR6(ctx: Aoc2020Part1Parser.R6Context?)

    /**
     * Enter a parse tree produced by [Aoc2020Part1Parser.r13].
     * @param ctx the parse tree
     */
    fun enterR13(ctx: Aoc2020Part1Parser.R13Context?)

    /**
     * Exit a parse tree produced by [Aoc2020Part1Parser.r13].
     * @param ctx the parse tree
     */
    fun exitR13(ctx: Aoc2020Part1Parser.R13Context?)

    /**
     * Enter a parse tree produced by [Aoc2020Part1Parser.r78].
     * @param ctx the parse tree
     */
    fun enterR78(ctx: Aoc2020Part1Parser.R78Context?)

    /**
     * Exit a parse tree produced by [Aoc2020Part1Parser.r78].
     * @param ctx the parse tree
     */
    fun exitR78(ctx: Aoc2020Part1Parser.R78Context?)

    /**
     * Enter a parse tree produced by [Aoc2020Part1Parser.r125].
     * @param ctx the parse tree
     */
    fun enterR125(ctx: Aoc2020Part1Parser.R125Context?)

    /**
     * Exit a parse tree produced by [Aoc2020Part1Parser.r125].
     * @param ctx the parse tree
     */
    fun exitR125(ctx: Aoc2020Part1Parser.R125Context?)

    /**
     * Enter a parse tree produced by [Aoc2020Part1Parser.r87].
     * @param ctx the parse tree
     */
    fun enterR87(ctx: Aoc2020Part1Parser.R87Context?)

    /**
     * Exit a parse tree produced by [Aoc2020Part1Parser.r87].
     * @param ctx the parse tree
     */
    fun exitR87(ctx: Aoc2020Part1Parser.R87Context?)

    /**
     * Enter a parse tree produced by [Aoc2020Part1Parser.r40].
     * @param ctx the parse tree
     */
    fun enterR40(ctx: Aoc2020Part1Parser.R40Context?)

    /**
     * Exit a parse tree produced by [Aoc2020Part1Parser.r40].
     * @param ctx the parse tree
     */
    fun exitR40(ctx: Aoc2020Part1Parser.R40Context?)

    /**
     * Enter a parse tree produced by [Aoc2020Part1Parser.r57].
     * @param ctx the parse tree
     */
    fun enterR57(ctx: Aoc2020Part1Parser.R57Context?)

    /**
     * Exit a parse tree produced by [Aoc2020Part1Parser.r57].
     * @param ctx the parse tree
     */
    fun exitR57(ctx: Aoc2020Part1Parser.R57Context?)

    /**
     * Enter a parse tree produced by [Aoc2020Part1Parser.r36].
     * @param ctx the parse tree
     */
    fun enterR36(ctx: Aoc2020Part1Parser.R36Context?)

    /**
     * Exit a parse tree produced by [Aoc2020Part1Parser.r36].
     * @param ctx the parse tree
     */
    fun exitR36(ctx: Aoc2020Part1Parser.R36Context?)

    /**
     * Enter a parse tree produced by [Aoc2020Part1Parser.r3].
     * @param ctx the parse tree
     */
    fun enterR3(ctx: Aoc2020Part1Parser.R3Context?)

    /**
     * Exit a parse tree produced by [Aoc2020Part1Parser.r3].
     * @param ctx the parse tree
     */
    fun exitR3(ctx: Aoc2020Part1Parser.R3Context?)

    /**
     * Enter a parse tree produced by [Aoc2020Part1Parser.r32].
     * @param ctx the parse tree
     */
    fun enterR32(ctx: Aoc2020Part1Parser.R32Context?)

    /**
     * Exit a parse tree produced by [Aoc2020Part1Parser.r32].
     * @param ctx the parse tree
     */
    fun exitR32(ctx: Aoc2020Part1Parser.R32Context?)

    /**
     * Enter a parse tree produced by [Aoc2020Part1Parser.r133].
     * @param ctx the parse tree
     */
    fun enterR133(ctx: Aoc2020Part1Parser.R133Context?)

    /**
     * Exit a parse tree produced by [Aoc2020Part1Parser.r133].
     * @param ctx the parse tree
     */
    fun exitR133(ctx: Aoc2020Part1Parser.R133Context?)

    /**
     * Enter a parse tree produced by [Aoc2020Part1Parser.r39].
     * @param ctx the parse tree
     */
    fun enterR39(ctx: Aoc2020Part1Parser.R39Context?)

    /**
     * Exit a parse tree produced by [Aoc2020Part1Parser.r39].
     * @param ctx the parse tree
     */
    fun exitR39(ctx: Aoc2020Part1Parser.R39Context?)

    /**
     * Enter a parse tree produced by [Aoc2020Part1Parser.r86].
     * @param ctx the parse tree
     */
    fun enterR86(ctx: Aoc2020Part1Parser.R86Context?)

    /**
     * Exit a parse tree produced by [Aoc2020Part1Parser.r86].
     * @param ctx the parse tree
     */
    fun exitR86(ctx: Aoc2020Part1Parser.R86Context?)

    /**
     * Enter a parse tree produced by [Aoc2020Part1Parser.r9].
     * @param ctx the parse tree
     */
    fun enterR9(ctx: Aoc2020Part1Parser.R9Context?)

    /**
     * Exit a parse tree produced by [Aoc2020Part1Parser.r9].
     * @param ctx the parse tree
     */
    fun exitR9(ctx: Aoc2020Part1Parser.R9Context?)

    /**
     * Enter a parse tree produced by [Aoc2020Part1Parser.r74].
     * @param ctx the parse tree
     */
    fun enterR74(ctx: Aoc2020Part1Parser.R74Context?)

    /**
     * Exit a parse tree produced by [Aoc2020Part1Parser.r74].
     * @param ctx the parse tree
     */
    fun exitR74(ctx: Aoc2020Part1Parser.R74Context?)

    /**
     * Enter a parse tree produced by [Aoc2020Part1Parser.r18].
     * @param ctx the parse tree
     */
    fun enterR18(ctx: Aoc2020Part1Parser.R18Context?)

    /**
     * Exit a parse tree produced by [Aoc2020Part1Parser.r18].
     * @param ctx the parse tree
     */
    fun exitR18(ctx: Aoc2020Part1Parser.R18Context?)

    /**
     * Enter a parse tree produced by [Aoc2020Part1Parser.r98].
     * @param ctx the parse tree
     */
    fun enterR98(ctx: Aoc2020Part1Parser.R98Context?)

    /**
     * Exit a parse tree produced by [Aoc2020Part1Parser.r98].
     * @param ctx the parse tree
     */
    fun exitR98(ctx: Aoc2020Part1Parser.R98Context?)

    /**
     * Enter a parse tree produced by [Aoc2020Part1Parser.r66].
     * @param ctx the parse tree
     */
    fun enterR66(ctx: Aoc2020Part1Parser.R66Context?)

    /**
     * Exit a parse tree produced by [Aoc2020Part1Parser.r66].
     * @param ctx the parse tree
     */
    fun exitR66(ctx: Aoc2020Part1Parser.R66Context?)

    /**
     * Enter a parse tree produced by [Aoc2020Part1Parser.r101].
     * @param ctx the parse tree
     */
    fun enterR101(ctx: Aoc2020Part1Parser.R101Context?)

    /**
     * Exit a parse tree produced by [Aoc2020Part1Parser.r101].
     * @param ctx the parse tree
     */
    fun exitR101(ctx: Aoc2020Part1Parser.R101Context?)

    /**
     * Enter a parse tree produced by [Aoc2020Part1Parser.r120].
     * @param ctx the parse tree
     */
    fun enterR120(ctx: Aoc2020Part1Parser.R120Context?)

    /**
     * Exit a parse tree produced by [Aoc2020Part1Parser.r120].
     * @param ctx the parse tree
     */
    fun exitR120(ctx: Aoc2020Part1Parser.R120Context?)

    /**
     * Enter a parse tree produced by [Aoc2020Part1Parser.r71].
     * @param ctx the parse tree
     */
    fun enterR71(ctx: Aoc2020Part1Parser.R71Context?)

    /**
     * Exit a parse tree produced by [Aoc2020Part1Parser.r71].
     * @param ctx the parse tree
     */
    fun exitR71(ctx: Aoc2020Part1Parser.R71Context?)

    /**
     * Enter a parse tree produced by [Aoc2020Part1Parser.r119].
     * @param ctx the parse tree
     */
    fun enterR119(ctx: Aoc2020Part1Parser.R119Context?)

    /**
     * Exit a parse tree produced by [Aoc2020Part1Parser.r119].
     * @param ctx the parse tree
     */
    fun exitR119(ctx: Aoc2020Part1Parser.R119Context?)

    /**
     * Enter a parse tree produced by [Aoc2020Part1Parser.r41].
     * @param ctx the parse tree
     */
    fun enterR41(ctx: Aoc2020Part1Parser.R41Context?)

    /**
     * Exit a parse tree produced by [Aoc2020Part1Parser.r41].
     * @param ctx the parse tree
     */
    fun exitR41(ctx: Aoc2020Part1Parser.R41Context?)

    /**
     * Enter a parse tree produced by [Aoc2020Part1Parser.r24].
     * @param ctx the parse tree
     */
    fun enterR24(ctx: Aoc2020Part1Parser.R24Context?)

    /**
     * Exit a parse tree produced by [Aoc2020Part1Parser.r24].
     * @param ctx the parse tree
     */
    fun exitR24(ctx: Aoc2020Part1Parser.R24Context?)

    /**
     * Enter a parse tree produced by [Aoc2020Part1Parser.r0].
     * @param ctx the parse tree
     */
    fun enterR0(ctx: Aoc2020Part1Parser.R0Context?)

    /**
     * Exit a parse tree produced by [Aoc2020Part1Parser.r0].
     * @param ctx the parse tree
     */
    fun exitR0(ctx: Aoc2020Part1Parser.R0Context?)

    /**
     * Enter a parse tree produced by [Aoc2020Part1Parser.r80].
     * @param ctx the parse tree
     */
    fun enterR80(ctx: Aoc2020Part1Parser.R80Context?)

    /**
     * Exit a parse tree produced by [Aoc2020Part1Parser.r80].
     * @param ctx the parse tree
     */
    fun exitR80(ctx: Aoc2020Part1Parser.R80Context?)

    /**
     * Enter a parse tree produced by [Aoc2020Part1Parser.r14].
     * @param ctx the parse tree
     */
    fun enterR14(ctx: Aoc2020Part1Parser.R14Context?)

    /**
     * Exit a parse tree produced by [Aoc2020Part1Parser.r14].
     * @param ctx the parse tree
     */
    fun exitR14(ctx: Aoc2020Part1Parser.R14Context?)

    /**
     * Enter a parse tree produced by [Aoc2020Part1Parser.r72].
     * @param ctx the parse tree
     */
    fun enterR72(ctx: Aoc2020Part1Parser.R72Context?)

    /**
     * Exit a parse tree produced by [Aoc2020Part1Parser.r72].
     * @param ctx the parse tree
     */
    fun exitR72(ctx: Aoc2020Part1Parser.R72Context?)

    /**
     * Enter a parse tree produced by [Aoc2020Part1Parser.r131].
     * @param ctx the parse tree
     */
    fun enterR131(ctx: Aoc2020Part1Parser.R131Context?)

    /**
     * Exit a parse tree produced by [Aoc2020Part1Parser.r131].
     * @param ctx the parse tree
     */
    fun exitR131(ctx: Aoc2020Part1Parser.R131Context?)

    /**
     * Enter a parse tree produced by [Aoc2020Part1Parser.r122].
     * @param ctx the parse tree
     */
    fun enterR122(ctx: Aoc2020Part1Parser.R122Context?)

    /**
     * Exit a parse tree produced by [Aoc2020Part1Parser.r122].
     * @param ctx the parse tree
     */
    fun exitR122(ctx: Aoc2020Part1Parser.R122Context?)

    /**
     * Enter a parse tree produced by [Aoc2020Part1Parser.r12].
     * @param ctx the parse tree
     */
    fun enterR12(ctx: Aoc2020Part1Parser.R12Context?)

    /**
     * Exit a parse tree produced by [Aoc2020Part1Parser.r12].
     * @param ctx the parse tree
     */
    fun exitR12(ctx: Aoc2020Part1Parser.R12Context?)

    /**
     * Enter a parse tree produced by [Aoc2020Part1Parser.r112].
     * @param ctx the parse tree
     */
    fun enterR112(ctx: Aoc2020Part1Parser.R112Context?)

    /**
     * Exit a parse tree produced by [Aoc2020Part1Parser.r112].
     * @param ctx the parse tree
     */
    fun exitR112(ctx: Aoc2020Part1Parser.R112Context?)

    /**
     * Enter a parse tree produced by [Aoc2020Part1Parser.r31].
     * @param ctx the parse tree
     */
    fun enterR31(ctx: Aoc2020Part1Parser.R31Context?)

    /**
     * Exit a parse tree produced by [Aoc2020Part1Parser.r31].
     * @param ctx the parse tree
     */
    fun exitR31(ctx: Aoc2020Part1Parser.R31Context?)

    /**
     * Enter a parse tree produced by [Aoc2020Part1Parser.r45].
     * @param ctx the parse tree
     */
    fun enterR45(ctx: Aoc2020Part1Parser.R45Context?)

    /**
     * Exit a parse tree produced by [Aoc2020Part1Parser.r45].
     * @param ctx the parse tree
     */
    fun exitR45(ctx: Aoc2020Part1Parser.R45Context?)

    /**
     * Enter a parse tree produced by [Aoc2020Part1Parser.r51].
     * @param ctx the parse tree
     */
    fun enterR51(ctx: Aoc2020Part1Parser.R51Context?)

    /**
     * Exit a parse tree produced by [Aoc2020Part1Parser.r51].
     * @param ctx the parse tree
     */
    fun exitR51(ctx: Aoc2020Part1Parser.R51Context?)

    /**
     * Enter a parse tree produced by [Aoc2020Part1Parser.r2].
     * @param ctx the parse tree
     */
    fun enterR2(ctx: Aoc2020Part1Parser.R2Context?)

    /**
     * Exit a parse tree produced by [Aoc2020Part1Parser.r2].
     * @param ctx the parse tree
     */
    fun exitR2(ctx: Aoc2020Part1Parser.R2Context?)

    /**
     * Enter a parse tree produced by [Aoc2020Part1Parser.r115].
     * @param ctx the parse tree
     */
    fun enterR115(ctx: Aoc2020Part1Parser.R115Context?)

    /**
     * Exit a parse tree produced by [Aoc2020Part1Parser.r115].
     * @param ctx the parse tree
     */
    fun exitR115(ctx: Aoc2020Part1Parser.R115Context?)

    /**
     * Enter a parse tree produced by [Aoc2020Part1Parser.r79].
     * @param ctx the parse tree
     */
    fun enterR79(ctx: Aoc2020Part1Parser.R79Context?)

    /**
     * Exit a parse tree produced by [Aoc2020Part1Parser.r79].
     * @param ctx the parse tree
     */
    fun exitR79(ctx: Aoc2020Part1Parser.R79Context?)

    /**
     * Enter a parse tree produced by [Aoc2020Part1Parser.r53].
     * @param ctx the parse tree
     */
    fun enterR53(ctx: Aoc2020Part1Parser.R53Context?)

    /**
     * Exit a parse tree produced by [Aoc2020Part1Parser.r53].
     * @param ctx the parse tree
     */
    fun exitR53(ctx: Aoc2020Part1Parser.R53Context?)

    /**
     * Enter a parse tree produced by [Aoc2020Part1Parser.r123].
     * @param ctx the parse tree
     */
    fun enterR123(ctx: Aoc2020Part1Parser.R123Context?)

    /**
     * Exit a parse tree produced by [Aoc2020Part1Parser.r123].
     * @param ctx the parse tree
     */
    fun exitR123(ctx: Aoc2020Part1Parser.R123Context?)

    /**
     * Enter a parse tree produced by [Aoc2020Part1Parser.r117].
     * @param ctx the parse tree
     */
    fun enterR117(ctx: Aoc2020Part1Parser.R117Context?)

    /**
     * Exit a parse tree produced by [Aoc2020Part1Parser.r117].
     * @param ctx the parse tree
     */
    fun exitR117(ctx: Aoc2020Part1Parser.R117Context?)

    /**
     * Enter a parse tree produced by [Aoc2020Part1Parser.r17].
     * @param ctx the parse tree
     */
    fun enterR17(ctx: Aoc2020Part1Parser.R17Context?)

    /**
     * Exit a parse tree produced by [Aoc2020Part1Parser.r17].
     * @param ctx the parse tree
     */
    fun exitR17(ctx: Aoc2020Part1Parser.R17Context?)
}
