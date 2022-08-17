import cv2
import torch 
import numpy as np
import sys


def main_predict(img_path,out_img):

    inputpath = "C:/Users/Administrator/Desktop/study/video/{}".format(img_path)
    cap = cv2.VideoCapture(inputpath)

    fps = cap.get(cv2.CAP_PROP_FPS)
    sizes = (int(cap.get(cv2.CAP_PROP_FRAME_WIDTH)), int(cap.get(cv2.CAP_PROP_FRAME_HEIGHT)))

    outpath = "C:/Users/Administrator/Desktop/study/videoresult/{}".format(out_img)
    videowriter = cv2.VideoWriter(outpath, cv2.VideoWriter_fourcc(*'H264'), fps, sizes)
    while cap.isOpened():
        # 导入模型
        model = torch.hub.load('yolov5', 'custom', path='yolov5/runs/train/exp/weights/last.pt', source="local",force_reload=True)

        ret, frame = cap.read()
        
        # Make detections 
        results = model(frame)

        #marked, frame = cap.read()
        
        videowriter.write(frame)
        
        cv2.imshow('YOLO', np.squeeze(results.render()))
        
        if cv2.waitKey(10) & 0xFF == ord('q'):
            break

        #filepath="C:/Users/Administrator/Desktop/study/videoresult/1.png"

        #cv2.imwrite(filepath, np.squeeze(results.render()))
        
        text = results.print()
        print( text)

    cap.release()
    cv2.destroyAllWindows()

if __name__ == '__main__':
    a = sys.argv[1]
    b = sys.argv[2]
    
    main_predict(a,b)

