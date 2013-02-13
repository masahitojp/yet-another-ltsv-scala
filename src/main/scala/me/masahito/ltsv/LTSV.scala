package me.masahito.ltsv

class LTSV  extends LTSVParser with LTSVFormatter {}

object LTSV{


  def apply(): LTSV = {
    new LTSV
  }
}
