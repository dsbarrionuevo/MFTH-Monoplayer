package mmorpg.map.buildingstrategies;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import mmorpg.common.AnimationHolder;
import mmorpg.enemies.Enemy;
import mmorpg.enemies.FoolEnemy;
import mmorpg.map.Map;
import mmorpg.map.room.Room;
import mmorpg.map.room.buildingstrategies.ImprovedFileRoomBuildingStrategy;
import mmorpg.map.room.buildingstrategies.RoomBuildingStrategy;
import mmorpg.map.tiles.DoorTile;
import mmorpg.map.tiles.Tile;
import mmorpg.mapreader.AnimationFile;
import mmorpg.mapreader.EnemyRoomFile;
import mmorpg.mapreader.MapFile;
import mmorpg.mapreader.MapReader;
import mmorpg.mapreader.RoomFile;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**
 *
 * @author Barrionuevo Diego
 */
public class ImprovedFileMapBuildingStrategy extends MapBuildingStrategy {

    private final String pathFile;

    public ImprovedFileMapBuildingStrategy(String pathFile, float tileWidth, float tileHeight) {
        super(1, tileWidth, tileHeight);
        this.pathFile = pathFile;
    }

    @Override
    public void build(Map map) {
        this.rooms = new ArrayList<>();
        MapReader mapReader = new MapReader();
        MapFile mapFile = mapReader.buildMapFormFile(pathFile);
        //creating the rooms
        int[][] mainMap = mapFile.getMap();
        for (int i = 0; i < mainMap.length; i++) {
            for (int j = 0; j < mainMap[0].length; j++) {
                RoomFile roomFile = mapFile.findRoomFile(mainMap[i][j]);
                if (roomFile != null) {
                    //the ImprovedFileRoomBuildingStrategy already draw the doorTile on the room
                    RoomBuildingStrategy roomBuildingStrategy = new ImprovedFileRoomBuildingStrategy(roomFile, tileWidth, tileHeight);
                    Room newRoom = new Room(roomFile.getId(), roomBuildingStrategy);
                    newRoom.setMap(map);
                    EnemyRoomFile[] enemiesRoomFile = roomFile.getEnemies();
                    for (int k = 0; k < enemiesRoomFile.length; k++) {
                        EnemyRoomFile enemyRoomFile = enemiesRoomFile[k];
                        Enemy enemy = new FoolEnemy();
                        if (enemyRoomFile.getEnemy().getEnemyType().getId() == 0) {//0 means wall enemy
                            //tendria que filtrar el tipe de enemigo
                            enemy.setSpeed((float) enemyRoomFile.getEnemy().getEnemyType().getSpeed());
                            //Animation[] animations = new Animation[enemyRoomFile.getEnemy().getAnimations().length];
                            int duration = 340;
                            AnimationHolder animation = new AnimationHolder();
                            ArrayList<String> animationNames = new ArrayList<>();
                            ArrayList<Animation> animations = new ArrayList<>();
                            for (int l = 0; l < enemyRoomFile.getEnemy().getAnimations().length; l++) {
                                String side = enemyRoomFile.getEnemy().getAnimations()[l].getSide();
                                animationNames.add(side);
                                AnimationFile animationFile = enemyRoomFile.getEnemy().getAnimations()[l].getAnimationFile();
                                String path;
                                Image[] sprites;
                                path = animationFile.getPath();
                                sprites = new Image[animationFile.getSprites().length];
                                for (int m = 0; m < sprites.length; m++) {
                                    try {
                                        sprites[m] = new Image(path + "/" + animationFile.getSprites()[m]);
                                    } catch (SlickException ex) {
                                        Logger.getLogger(ImprovedFileMapBuildingStrategy.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }
                                animations.add(new Animation(sprites, duration, true));
                            }
                            animation.setup(animationNames, animations, new int[]{0, 0, 0, 0});
                            enemy.setAnimation(animation);
                            enemy.setGraphic(enemy.getAnimation().changeAnimation(animationNames.get(0)));//otherwise, write "front" here
                        }
                        newRoom.addObject(enemy, enemyRoomFile.getPosition().getX(), enemyRoomFile.getPosition().getY());
                    }
                    rooms.add(newRoom);
                    //for now, when 1 is the id of room, I say its the first room
                    if (roomFile.getId() == 1) {
                        this.firstRoom = newRoom;
                    }
                }
            }
        }
        if (this.firstRoom == null) {
            this.firstRoom = rooms.get(0);
        }
        //connecting rooms
        //this only works if the rooms are neighboors and each one has only one door on his side
        for (int i = 0; i < mainMap.length; i++) {
            for (int j = 0; j < mainMap[0].length; j++) {
                if (mainMap[i][j] > 0) {
                    Room roomOne = getRoomById(mainMap[i][j]);
                    //right
                    if (j < mainMap[0].length - 1 && mainMap[i][j + 1] > 0) {
                        Room roomTwo = getRoomById(mainMap[i][j + 1]);
                        Tile[] doorTilesRoomOne = roomOne.getTilesOfType(Tile.DOOR_TILE);
                        Tile[] doorTilesRoomTwo = roomTwo.getTilesOfType(Tile.DOOR_TILE);
                        DoorTile doorRoomOne = null, doorRoomTwo = null;
                        for (int k = 0; k < doorTilesRoomOne.length; k++) {
                            if (doorTilesRoomOne[k].getTileX() == roomOne.getRoomWidth() - 1) {
                                doorRoomOne = (DoorTile) doorTilesRoomOne[k];
                                break;
                            }
                        }
                        for (int k = 0; k < doorTilesRoomTwo.length; k++) {
                            if (doorTilesRoomTwo[k].getTileX() == 0) {
                                doorRoomTwo = (DoorTile) doorTilesRoomTwo[k];
                                break;
                            }
                        }
                        if (doorRoomOne != null && doorRoomTwo != null) {
                            connectRooms(roomOne, doorRoomOne.getTileX(), doorRoomOne.getTileY(), roomTwo, doorRoomTwo.getTileX(), doorRoomTwo.getTileY());
                        }
                    }
                    //down
                    if (i < mainMap.length - 1 && mainMap[i + 1][j] > 0) {
                        Room roomTwo = getRoomById(mainMap[i + 1][j]);
                        Tile[] doorTilesRoomOne = roomOne.getTilesOfType(Tile.DOOR_TILE);
                        Tile[] doorTilesRoomTwo = roomTwo.getTilesOfType(Tile.DOOR_TILE);
                        DoorTile doorRoomOne = null, doorRoomTwo = null;
                        for (int k = 0; k < doorTilesRoomOne.length; k++) {
                            if (doorTilesRoomOne[k].getTileY() == roomOne.getRoomHeight() - 1) {
                                doorRoomOne = (DoorTile) doorTilesRoomOne[k];
                                break;
                            }
                        }
                        for (int k = 0; k < doorTilesRoomTwo.length; k++) {
                            if (doorTilesRoomTwo[k].getTileY() == 0) {
                                doorRoomTwo = (DoorTile) doorTilesRoomTwo[k];
                                break;
                            }
                        }
                        if (doorRoomOne != null && doorRoomTwo != null) {
                            connectRooms(roomOne, doorRoomOne.getTileX(), doorRoomOne.getTileY(), roomTwo, doorRoomTwo.getTileX(), doorRoomTwo.getTileY());
                        }
                    }
                }
            }
        }
    }

}
