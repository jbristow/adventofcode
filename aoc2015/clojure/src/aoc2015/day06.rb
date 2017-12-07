
def get_starting_grid
  start_grid = {} 
  1000.times do |x|
    1000.times do |y|
      start_grid["#{x}-#{y}"] = false
    end
  end
  start_grid
end

def parse(s) 
  /(turn on|turn off|toggle) (\d+),(\d+) through (\d+),(\d+)/.match(s)
end

def change(x1, y1, x2, y2, state, grid)
  (x1..x2).each do |x|
    (y1..y2).each do |y|
      key = "#{x}-#{y}"
      if state == "turn on" then
        grid[key] = true
      elsif state == "turn off" then
        grid[key] = false
      else
        grid[key] = !grid[key]
      end
    end
  end
end

grid = get_starting_grid()
File.readlines('resources/day06.input').each do |line|
  puts "Reading line \"#{line}\""
  vars = parse(line)
  change(vars[2], vars[3], vars[4], vars[5], vars[1], grid)

end

puts grid.values.count{|i| i}
