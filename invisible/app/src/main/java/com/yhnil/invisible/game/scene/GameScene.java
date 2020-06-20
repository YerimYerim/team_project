package com.yhnil.invisible.game.scene;

import android.content.Context;
import android.graphics.Color;
import android.graphics.RectF;
import android.media.MediaPlayer;

import com.yhnil.invisible.R;
import com.yhnil.invisible.framework.main.GameTimer;
import com.yhnil.invisible.framework.main.UiBridge;
import com.yhnil.invisible.framework.obj.ScoreObject;
import com.yhnil.invisible.framework.obj.ui.Joystick;
import com.yhnil.invisible.framework.view.GameView;
import com.yhnil.invisible.game.obj.Player;
import com.yhnil.invisible.game.obj.sobj.Core;
import com.yhnil.invisible.game.obj.sobj.CoreStone;
import com.yhnil.invisible.game.obj.sobj.DangerZone;
import com.yhnil.invisible.game.obj.sobj.PlayGround;
import com.yhnil.invisible.game.obj.sobj.Stone;

public class GameScene extends com.yhnil.invisible.framework.main.GameScene {
    private static final String TAG = GameScene.class.getSimpleName();
    private Joystick joystick;
    public ScoreObject scoreObject;
    public DangerZone dangerZone;
    public Core core;
    private GameTimer feverTimer;
    private MediaPlayer mediaPlayer;
    private  Context context;

    public enum Layer {
        bg, corestone, light, stone, player, ui, joystick, COUNT
    }

    private Player player;

    private GameTimer timer;
    private static GameScene instance;
    @Override
    protected int getLayerCount() {
        return Layer.COUNT.ordinal();
    }

    @Override
    public void update() {
        super.update();
        if (timer.done()) {
            gameWorld.add(Layer.stone.ordinal(), new Stone(0, 0));
            timer.reset();
        }
        if (feverTimer.done()&& core.grayZoneIndex==6){
            gameWorld.add(Layer.stone.ordinal(), new Stone(0, 0));
            feverTimer.reset();
        }
    }

    @Override
    public void enter() {
        super.enter();
        instance = this;
        initObjects();
    }

    @Override
    public void exit() {
        mediaPlayer.release();
        super.exit();

    }

    private void initObjects() {
        mediaPlayer = GameView.soundMusic.play(R.raw.ingame);
        mediaPlayer.start();
        RectF rbox = new RectF(UiBridge.x(-52), UiBridge.y(0), UiBridge.x(-20), UiBridge.y(62));
        scoreObject = new ScoreObject(R.mipmap.number_64x84, rbox);
        gameWorld.add(Layer.ui.ordinal(), scoreObject);
        timer = new GameTimer(3, 1);
        feverTimer = new GameTimer(1 ,1);
        gameWorld.add(GameScene.Layer.bg.ordinal(), new PlayGround(0, 0));

        core = new Core(0, 0);
        gameWorld.add(Layer.corestone.ordinal(), core);

        dangerZone = new DangerZone(0, 0);
        core.connectDangerZone(dangerZone);
        gameWorld.add(Layer.light.ordinal(), dangerZone);


        player = new Player(0, 50);
        player.connectCcore(core);
        gameWorld.add(Layer.player.ordinal(), player);

        joystick = new Joystick();
        player.connectJoystick(joystick);
        gameWorld.add(Layer.joystick.ordinal(), joystick);

        int colors[] = {
                Color.RED,
                Color.GREEN,
                Color.BLUE,
                Color.CYAN,
                Color.MAGENTA,
                Color.YELLOW
        };

        int index = 0;
        for(int color : colors)
        {
            CoreStone coreStone = new CoreStone(0, 0,color);
            core.connectCoreStone(coreStone, index++);
            gameWorld.add(GameScene.Layer.corestone.ordinal(), coreStone);
        }

    }

    public static GameScene get() {
        return instance;
    }
}