import os 
import glob
from PIL import Image

with glob.glob("runs/detect/*") as f:
    print(f)