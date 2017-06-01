package engine;

/**
 * Created by Jerome on 27/05/2017.
 */
public interface GameApplication {
    void init(Window window);
    void input(Window window);
    void update(float delta);
    void render(Window window);
    void cleanup();
}
