class Node
  attr_reader :label, :expression, :depends_on
  def initialize(label, expression, depends_on)
    @label = label
    @expression = expression
    @depends_on = depends_on
  end

end
