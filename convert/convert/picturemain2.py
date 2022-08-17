from pip import main
import torch 
import matplotlib.pyplot as plt 
import numpy as np
import cv2
import os 
import sys,os 
import shutil
import io
import sys



def main_predict(img_path):
    
    path = "C:/Users/Administrator/Desktop/study/picture/{}".format(img_path)
    
    # 导入模型
    model = torch.hub.load('yolov5', 'custom', path='yolov5/runs/train/exp/weights/last.pt', source="local",force_reload=True)

    #img = os.path.join('data/emo/test/images/happy-d0df666c-eb8e-11ec-81f1-acde48001122_jpg.rf.8c1d95d4903aaa819934ce6b25bad6ee.jpg')
    img = os.path.join(path)
    results = model(img)
    # 输出日志
    print(results.print())
    # 保存图片
    results.save("output.png")
    
    #shutil.rmtree()
    print("done")



 
if __name__ == '__main__':
    a = sys.argv[1]
    main_predict(a)
