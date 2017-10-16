package com.ly.immortal.domain.test

import java.io.PrintStream

interface Quest{
    fun embarkQuest()
}

data class SlaryDragonQuest(val printStream: PrintStream) : Quest {
    override fun embarkQuest() = printStream.println("勇士轻而易举的杀死了巨龙")
}
data class TestQuest(val message:String) : Quest {
    override fun embarkQuest() = println(message)
}

data class BraveKnight(val quest: Quest){
    fun embarkQuest() = quest.embarkQuest()
}

data class Minstrel(val printStream: PrintStream){
    fun singBeforeQuest() = printStream.println("勇敢的骑士踏上冒险的征程")
    fun singAfterQuest() = printStream.println("勇敢的骑士完成了冒险")
}


