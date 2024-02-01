package com.badlogic.drop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class CitadelWindow extends Stage {
    private Skin skin;
    private Window window;
    private Label ratingLabel;
    private TextButton closeButton;
    public boolean isWindowOpen;

    private long copper_bal;
    private long iron_bal;
    private long gold_bal;
    private long money;
    public int rating;
    private boolean build_flag1;
    private boolean build_flag2;
    private boolean build_flag3;
    private Label ratingExplanationLabel;

    public CitadelWindow(long copper_bal, long iron_bal, long gold_bal, long money, boolean build_flag1, boolean build_flag2, boolean build_flag3) {
        this.copper_bal = copper_bal;
        this.iron_bal = iron_bal;
        this.gold_bal = gold_bal;
        this.money = money;
        this.build_flag1 = build_flag1;
        this.build_flag2 = build_flag2;
        this.build_flag3 = build_flag3;
        this.rating = calculateRating();

        skin = new Skin(Gdx.files.internal("authPicture/uiskin.json"));
        initialize();
    }

    private int calculateRating() {
        int initialRating = 0;
        // Добавляем рейтинг за постройки
        if (build_flag1) initialRating += 1;
        if (build_flag2) initialRating += 2;
        if (build_flag3) initialRating += 3;
        // Добавляем рейтинг за ресурсы
        initialRating += copper_bal / 1000 * 2;
        initialRating += iron_bal / 500 * 3;
        initialRating += gold_bal / 100 * 5;
        initialRating += money / 1000000 * 10;
        return initialRating;
    }

    private void initialize() {
        window = new Window("Your personal rating", skin);
        window.setSize(1500, 1000);
        window.setPosition(600, 125);

        rating = calculateRating();
        ratingLabel = new Label("Rating: " + rating, skin);
        ratingLabel.setStyle(new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        ratingLabel.setFontScale(3f);

        // Строка с описанием формирования рейтинга
        String ratingExplanationText = "Rating is calculated as follows:\n" +
                "- 1 point for each Building Flag 1\n" +
                "- 2 points for each Building Flag 2\n" +
                "- 3 points for each Building Flag 3\n" +
                "- 2 points for every 1000 Copper\n" +
                "- 3 points for every 500 Iron\n" +
                "- 5 points for every 100 Gold\n" +
                "- 10 points for every 1,000,000 Money";
        ratingExplanationLabel = new Label(ratingExplanationText, skin);
        ratingExplanationLabel.setStyle(new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        ratingExplanationLabel.setFontScale(2f);
        ratingExplanationLabel.setWrap(true);

        closeButton = new TextButton("Close", skin);
        closeButton.getLabel().setFontScale(2f);
        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                hide();
            }
        });

        Table contentTable = new Table();
        contentTable.add(ratingLabel).padBottom(20).row();
        contentTable.add(ratingExplanationLabel).padBottom(20).width(1400).row();
        contentTable.add(closeButton).padTop(50).colspan(2).center();

        window.add(contentTable).expand().fill();
        addActor(window);
    }

    public void update(long copper_bal, long iron_bal, long gold_bal, long money, boolean build_flag1, boolean build_flag2, boolean build_flag3) {
        this.copper_bal = copper_bal;
        this.iron_bal = iron_bal;
        this.gold_bal = gold_bal;
        this.money = money;
        this.build_flag1 = build_flag1;
        this.build_flag2 = build_flag2;
        this.build_flag3 = build_flag3;

        rating = calculateRating();
        ratingLabel.setText("Rating: " + rating);
    }

    public void show() {
        window.setVisible(true);
        isWindowOpen = true;
    }

    public void hide() {
        isWindowOpen = false;
        window.setVisible(false);
    }
}
