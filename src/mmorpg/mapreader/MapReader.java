package mmorpg.mapreader;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import system.SystemIO;

/**
 *
 * @author Barrionuevo Diego
 */
public class MapReader {

    private MapFile map;

    public MapReader() {

    }

    public MapFile buildMapFormFile(String mapFileName) {
        try {
            String mapFile = SystemIO.readFile(mapFileName);
            map = new MapFile();
            JSONObject root = new JSONObject(mapFile.trim());
            map.setId(root.getInt("id_map"));
            map.setMap(getMap(root, "map"));
            JSONArray tileTypesJson = root.getJSONArray("tile_types");
            TileType[] tileTypes = new TileType[tileTypesJson.length()];
            for (int i = 0; i < tileTypesJson.length(); i++) {
                JSONObject currentTileTypeJson = tileTypesJson.getJSONObject(i);
                tileTypes[i] = new TileType(
                        currentTileTypeJson.getInt("id_tile_type"),
                        currentTileTypeJson.getString("name"),
                        currentTileTypeJson.getBoolean("walkable"),
                        currentTileTypeJson.getBoolean("door")
                );
            }
            map.setTileTypes(tileTypes);

            JSONArray enemyTypesJson = root.getJSONArray("enemy_types");
            EnemyType[] enemyTypes = new EnemyType[enemyTypesJson.length()];
            for (int i = 0; i < enemyTypesJson.length(); i++) {
                JSONObject currentEnemyTypeJson = enemyTypesJson.getJSONObject(i);
                enemyTypes[i] = new EnemyType(
                        currentEnemyTypeJson.getInt("id_enemy_type"),
                        currentEnemyTypeJson.getString("name"),
                        currentEnemyTypeJson.getDouble("speed")
                );
            }
            map.setEnemyTypes(enemyTypes);

            JSONArray roomTypesJson = root.getJSONArray("room_types");
            RoomType[] roomTypes = new RoomType[roomTypesJson.length()];
            for (int i = 0; i < roomTypesJson.length(); i++) {
                JSONObject currentRoomTypeJson = roomTypesJson.getJSONObject(i);
                JSONArray tilesFileJson = currentRoomTypeJson.getJSONArray("tiles");
                TileFile[] tilesFile = new TileFile[tilesFileJson.length()];
                for (int j = 0; j < tilesFileJson.length(); j++) {
                    JSONObject currentTileFileJson = tilesFileJson.getJSONObject(j);
                    tilesFile[j] = new TileFile(
                            currentTileFileJson.getInt("id_tile"),
                            map.findTileType(currentTileFileJson.getInt("tile_type")),
                            currentTileFileJson.getString("image")
                    );
                }
                EnemyFile[] enemiesFile = new EnemyFile[0];
                if (currentRoomTypeJson.has("enemies")) {
                    JSONArray enemiesFileJson = currentRoomTypeJson.getJSONArray("enemies");
                    enemiesFile = new EnemyFile[enemiesFileJson.length()];
                    for (int j = 0; j < enemiesFileJson.length(); j++) {
                        JSONObject currentEnemyFileJson = enemiesFileJson.getJSONObject(j);
                        JSONArray animationsJson = currentEnemyFileJson.getJSONArray("animations");
                        Animation[] animations = new Animation[animationsJson.length()];
                        for (int k = 0; k < animationsJson.length(); k++) {
                            JSONObject currentAnimationJson = animationsJson.getJSONObject(k);
                            JSONObject animationFileJson = currentAnimationJson.getJSONObject("animation");
                            JSONArray spritesJson = animationFileJson.getJSONArray("sprites");
                            String[] sprites = new String[spritesJson.length()];
                            for (int l = 0; l < spritesJson.length(); l++) {
                                sprites[l] = spritesJson.get(l).toString();
                            }
                            AnimationFile animationFile = new AnimationFile(animationFileJson.getString("path"), sprites, animationFileJson.getString("still"));
                            Animation animation = new Animation(currentAnimationJson.getString("side"), animationFile);
                            animations[k] = animation;
                        }
                        enemiesFile[j] = new EnemyFile(
                                currentEnemyFileJson.getInt("id_enemy"),
                                currentEnemyFileJson.getString("name"),
                                map.findEnemyType(currentEnemyFileJson.getInt("enemy_type")),
                                animations
                        );
                    }
                }
                roomTypes[i] = new RoomType(
                        currentRoomTypeJson.getInt("id_room_type"),
                        currentRoomTypeJson.getString("name"),
                        tilesFile,
                        enemiesFile
                );
            }
            map.setRoomTypes(roomTypes);

            JSONArray roomsJson = root.getJSONArray("rooms");
            RoomFile[] rooms = new RoomFile[roomsJson.length()];
            for (int i = 0; i < roomsJson.length(); i++) {
                JSONObject currentRoomFileJson = roomsJson.getJSONObject(i);
                EnemyRoomFile[] enemiesRoomFile = new EnemyRoomFile[0];
                if (currentRoomFileJson.has("enemies")) {
                    JSONArray enemiesRoomFileJson = currentRoomFileJson.getJSONArray("enemies");
                    enemiesRoomFile = new EnemyRoomFile[enemiesRoomFileJson.length()];
                    for (int j = 0; j < enemiesRoomFileJson.length(); j++) {
                        JSONObject currentEnemyFileJson = enemiesRoomFileJson.getJSONObject(j);
                        JSONObject positionFileJson = currentEnemyFileJson.getJSONObject("position");
                        PositionFile positionFile = new PositionFile(positionFileJson.getInt("x"), positionFileJson.getInt("y"));
                        enemiesRoomFile[j] = new EnemyRoomFile(
                                map.findRoomType(currentRoomFileJson.getInt("id_room")).findEnemyFile(currentEnemyFileJson.getInt("id_enemy")),
                                positionFile
                        );
                    }
                }
                rooms[i] = new RoomFile(
                        currentRoomFileJson.getInt("id_room"),
                        map.findRoomType(currentRoomFileJson.getInt("room_type")),
                        enemiesRoomFile,
                        getMap(currentRoomFileJson, "map")
                );
            }
            map.setRooms(rooms);
        } catch (Exception ex) {
            Logger.getLogger(MapReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return this.map;
    }

    public static int[][] getMap(JSONObject obj, String mapName) {
        int[][] mapCoors;
        JSONArray mapRows = obj.getJSONArray(mapName);
        mapCoors = new int[mapRows.length()][mapRows.getJSONArray(0).length()];
        for (int i = 0; i < mapRows.length(); i++) {
            JSONArray currentRow = mapRows.getJSONArray(i);
            for (int j = 0; j < currentRow.length(); j++) {
                mapCoors[i][j] = Integer.parseInt(currentRow.get(j).toString());
            }
        }
        return mapCoors;
    }

    public static void main(String[] args) {
        new MapReader().buildMapFormFile("res/map2.txt");
    }
}
