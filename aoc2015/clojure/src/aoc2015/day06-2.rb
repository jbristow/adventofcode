
def get_starting_grid
  start_grid = {} 
  1000.times do |x|
    1000.times do |y|
      start_grid["#{x}-#{y}"] = 0
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
      grid[key] = 0 unless grid.has_key? key
      if state == "turn on" then
        grid[key] =  grid[key] + 1
      elsif state == "turn off" then
        grid[key] = [grid[key] - 1, 0].max
      else
        grid[key] = grid[key] + 2
      end
    end
  end
end

grid = {}
File.readlines('resources/day06.input').each do |line|
  puts "Reading line \"#{line.chomp}\""
  vars = parse(line)
  change(vars[2], vars[3], vars[4], vars[5], vars[1], grid)

end

puts grid.values.inject(0){|sum, i| sum + i}

