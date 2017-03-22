# LayerList
- Contains layers indexed by order

# Layer
- Contains LayerComponent[]

# LayerComponent
- Abstract class for the various types of overlay components

- Each must implement draw method


## RectangleComponent
- Contains (x1,y1,x2,y2) coordinates

---

# Timeframe
- Contains Layer[]
- Contains date and time representation using Java's Calendar framework

# Timesegment
- List of Timeframe objects to represent a particular period in history