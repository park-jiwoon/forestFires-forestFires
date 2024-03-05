from flask import Flask, request, jsonify
from PIL import Image
import io
from ultralytics import YOLO  # 가상의 API 호출 방식

app = Flask(__name__)

model = YOLO('best.pt')  # 모델 파일명에 맞게 수정

@app.route('/detect', methods=['POST'])
def detect():
    if 'file' not in request.files:
        return jsonify({'error': 'file is missing'}), 400

    file = request.files['file']
    if file.filename == '':
        return jsonify({'error': 'file is missing'}), 400

    image = Image.open(io.BytesIO(file.read()))

    # YOLO 객체 탐지 실행
    results = model(image)  # 실제 API 사용 방식에 따라 조정 필요

    detected = {'smoke': False, 'fire': False}
    for result in results:
        if result:
            for box in result.boxes:
                cls = int(box.cls.item())
                if cls == 0:
                    detected['smoke'] = True
                elif cls == 1:
                    detected['fire'] = True

    if detected['smoke'] and detected['fire']:
        message = 'fire and smoke'
    elif detected['smoke']:
        message = 'smoke'
    elif detected['fire']:
        message = 'fire'
    else:
        message = 'no object'

    return jsonify({'message': message})

if __name__ == '__main__':
    app.run(debug=True)
