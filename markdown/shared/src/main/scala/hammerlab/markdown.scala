package hammerlab

import org.hammerlab.markdown.table.HasTable

trait markdown {
  trait table extends HasTable
  object table extends table
}

object markdown extends markdown with HasTable
