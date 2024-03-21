from nav2_msgs.srv import LoadMap
import rclpy
from rclpy.node import Node
from flask import Flask, request, send_file
from werkzeug.utils import secure_filename
from pysondb import db
import os


# Константы
MAP_ALLOWED_EXTENSIONS = {'png', 'pgm', 'bmp'} # разрешённые расширение карт
YAML_ALLOWED_EXTENSIONS = {'yaml', 'txt'} # разрешённые расширение описания карт 
UPLOAD_FOLDER = '/maps' # папка, где содержатся файлы
maps = db.getDb("/maps.json") # pysondb, крч бд в json
app = Flask(__name__) # app flask'a - выделен вне, для работы нескольких нод одновременно с одним flask app

def allowed_map_file(filename: str) -> bool:
    return '.' in filename and \
           filename.rsplit('.', 1)[1].lower() in MAP_ALLOWED_EXTENSIONS


def allowed_yaml_file(filename: str) -> bool:
    return '.' in filename and \
           filename.rsplit('.', 1)[1].lower() in YAML_ALLOWED_EXTENSIONS

class MapLoader(Node):
    
    def __init__(self):
        super().__init__('map_helper')
        self.cli = self.create_client(LoadMap, '/map_server/load_map')
        while not self.cli.wait_for_service(timeout_sec=1.0):
            self.get_logger().info('service not available, waiting again...')

        # создание папки с картами
        try:
            os.mkdir("/maps")
        except OSError:
            pass
        self.req = LoadMap.Request()

        # создание route
        @app.route("/load_map", methods=["POST"])
        def load_map():
            mapName = request.form["map_name"]
            yamlFile = request.files["yaml"]
            mapFile = request.files["map"]

            # картинка карты нужного формата?
            if not mapFile or not allowed_map_file(mapFile.filename):
                return "bad map file", 400

            # yaml-карты нужного формата?
            if not yamlFile or not allowed_yaml_file(yamlFile.filename):
                return "bad yaml file", 400

            # исправление имён файлов
            mapPath = os.path.join(UPLOAD_FOLDER, secure_filename(mapFile.filename))
            yamlPath = os.path.join(UPLOAD_FOLDER, secure_filename(yamlFile.filename))

            # существуют ли карты с таким именем
            if len(maps.getBy({"name":mapName})) > 0:
                return "map name dublication", 400
            mapFile.save(mapPath)
            yamlFile.save(yamlPath)
            maps.add({"name": mapName, "yamlPath": yamlPath,
                      "mapPath": mapPath})
            return str(self.request_service(os.path.join(UPLOAD_FOLDER,
                                                         yamlPath)).result)
        

    # вызов сервиса с путём до файла с картой
    def request_service(self, yamlFileName: str):
        self.req.map_url = yamlFileName
        self.future = self.cli.call_async(self.req)
        rclpy.spin_until_future_complete(self, self.future)
        return self.future.result()

@app.route("/all_maps", methods=["GET"])
def allMaps():
    return maps.getAll()

@app.route("/yaml/<mapName>", methods=["GET"])
def getYamlFile(mapName: str):
    map_ = maps.getBy({"name":mapName})
    if map_ == []: return "map not found", 400
    return send_file(map_[0]["yamlPath"])

@app.route("/image/<mapName>", methods=["GET"])
def getImage(mapName: str):
    map_ = maps.getBy({"name":mapName})
    if map_ == []: return "map not found", 400
    return send_file(map_[0]["mapPath"])
    
def main():
    rclpy.init()
    map_loader = MapLoader()
    app.run(debug=True, use_reloader=False)


    
if __name__ == '__main__':
    main()
