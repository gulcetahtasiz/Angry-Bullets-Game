import java.awt.*;
import java.awt.event.KeyEvent;

    /**
     * Angry Bullets Game
     * @author Gülce Tahtasız, Student ID: 2022400234
     * @since Date: 20.03.2024
     */

    public class GulceTahtasiz {
        public static void main(String[] args) {

            // Game Parameters
            int width = 1600; // screen width
            int height = 800; // screen height
            double gravity = 9.80665; // gravity
            double x0 = 120; // x and y coordinates of the bullet’s starting position on the platform
            double y0 = 120;
            double bulletVelocity = 180; // initial velocity
            double bulletAngle = 45.0; // initial angle

            // Canvas
            StdDraw.setCanvasSize(width, height);  // Sets the canvas size.
            StdDraw.setXscale(0, width); // Sets the range of x scale
            StdDraw.setYscale(0, height); // Sets the range of y scale
            StdDraw.enableDoubleBuffering(); // Smooths the animation.

            // Box coordinates for obstacles.
            // Each row stores a box contains x and y coordinates of the lower left rectangle corner, width, and height.
            double[][] obstacleArray = {
                    {1200, 0, 60, 220},
                    {1000, 0, 60, 160},
                    {600, 0, 60, 80},
                    {600, 180, 60, 160},
                    {220, 0, 120, 180},
            };

            // Box coordinates for targets.
            // Each row stores a box contains x and y coordinates of the lower left rectangle corner, width, and height.
            double[][] targetArray = {
                    {1160, 0, 30, 30},
                    {730, 0, 30, 30},
                    {150, 0, 20, 20},
                    {1480, 0, 60, 60},
                    {340, 80, 60, 30},
                    {1500, 600, 60, 60},
            };

            boolean gameRunning = true; // Will return true as long as the game continues.

            // Animation.
            while (true) {

                if (gameRunning) {
                    // GAME ENVIRONMENT
                    StdDraw.clear(StdDraw.WHITE);

                    // Platform
                    StdDraw.setPenColor(StdDraw.BLACK);
                    StdDraw.filledRectangle(60, 60, 60, 60);

                    // Obstacles
                    StdDraw.setPenColor(StdDraw.DARK_GRAY); // Creates all the obstacles.
                    for (double[] obstacles : obstacleArray) {
                        StdDraw.filledRectangle(obstacles[0] + (obstacles[2] / 2), obstacles[1] + (obstacles[3] / 2), obstacles[2] / 2, obstacles[3] / 2);
                    }

                    // Targets
                    StdDraw.setPenColor(StdDraw.PRINCETON_ORANGE); // Creates all the obstacles.
                    for (double[] targets : targetArray) {
                        StdDraw.filledRectangle(targets[0] + (targets[2] / 2), targets[1] + (targets[3] / 2), targets[2] / 2, targets[3] / 2);
                    }

                    //KEYBOARD COMMANDS
                    // Press up to increase angle.
                    if (StdDraw.isKeyPressed(KeyEvent.VK_UP)) {
                        bulletAngle = bulletAngle + 1.0;
                    }
                    // Press down to decrease angle.
                    if (StdDraw.isKeyPressed(KeyEvent.VK_DOWN)) {
                        bulletAngle = bulletAngle - 1.0;
                    }
                    // Press right to increase velocity.
                    if (StdDraw.isKeyPressed(KeyEvent.VK_RIGHT)) {
                        bulletVelocity = bulletVelocity + 1.0;
                    }
                    // Press left to decrease velocity.
                    if (StdDraw.isKeyPressed(KeyEvent.VK_LEFT)) {
                        bulletVelocity = bulletVelocity - 1.0;
                    }

                    // Indicators of velocity and angle.
                    StdDraw.setPenColor(StdDraw.WHITE);
                    StdDraw.setFont( new Font("angryBulletsGame", Font.BOLD, 15) );
                    StdDraw.textLeft(20, 40, "v: " + Double.toString(bulletVelocity));
                    StdDraw.textLeft(20,60,"a: " + Double.toString(bulletAngle));

                    // My variables
                    double x1 = x0 + ((bulletVelocity/1.725) * Math.cos(Math.toRadians(bulletAngle))); // horizontal position of the ball.
                    double y1 = y0 + ((bulletVelocity/1.725) * Math.sin(Math.toRadians(bulletAngle))); // horizontal position of the ball.
                    double time = 0.0; // Starting time variable.
                    double ballSize = 4; // Random size.

                    // Shooting Line
                    StdDraw.setPenColor(StdDraw.BLACK);
                    StdDraw.setPenRadius(0.01);
                    StdDraw.line(x0, y0, x1, y1);

                    // Projectile Motion
                    if (StdDraw.isKeyPressed(KeyEvent.VK_SPACE)) {
                        StdDraw.setPenColor(StdDraw.BLACK);
                        StdDraw.setPenRadius(0.005);

                        // Loop for shooting animation.
                        while (true) {
                            double positionX = x0 + ((bulletVelocity/1.725) * (Math.cos(Math.toRadians(bulletAngle)) * time));
                            double positionY = y0 + ((bulletVelocity/1.725)* (Math.sin(Math.toRadians(bulletAngle)) * time) + (-1 * 0.5 * gravity * time * time));

                            StdDraw.filledCircle(positionX, positionY, ballSize);
                            StdDraw.line(positionX, positionY, x1, y1); // Creates the line between each ball simulation.
                            StdDraw.pause(20); //
                            StdDraw.show();

                            time += 0.2; // My experimental time interval that determines distance between each ball simulations.
                            x1 = positionX;
                            y1 = positionY;


                            // Shoots
                            boolean obstacleHit = false; // Stays false while the ball doesn't hit any obstacle.
                            boolean targetHit = false; // Stays false while the ball doesn't hit any target.

                            for (double[] obstacles : obstacleArray) {
                                double closestX = Math.max(obstacles[0], Math.min(x1, obstacles[0] + obstacles[2])); // If center is in the obstacle, then x1 will be chosen here.
                                double closestY = Math.max(obstacles[1], Math.min(y1, obstacles[1] + obstacles[3])); // If center is in the obstacle, then y1 will be chosen here.
                                double distanceX = x1 - closestX; //therefore, if center is in the obstacle, distanceX = 0 and distanceY=0.
                                double distanceY = y1 - closestY;
                                double totalDistance= distanceY + distanceX;
                                if (totalDistance == 0) {
                                    obstacleHit = true;
                                    break;
                                }
                            }

                            for (double[] targets : targetArray) {
                                double closestX2 = Math.max(targets[0], Math.min(x1, targets[0] + targets[2])); // If center is in the target, then x1 will be chosen here.
                                double closestY2 = Math.max(targets[1], Math.min(y1, targets[1] + targets[3])); // If center is in the target, then x1 will be chosen here.
                                double distanceX2 = x1 - closestX2; //therefore, if center is in the target, distanceX = 0 and distanceY=0.
                                double distanceY2 = y1 - closestY2;
                                double totalDistance2= distanceY2 + distanceX2;
                                if (totalDistance2 == 0) {
                                    targetHit = true;
                                    break;
                                }
                            }

                            if (obstacleHit) { // If obstacleHit returns true above, this function will write "Hit an obstacle. Press 'r' to shoot again." to the left top corner.
                                StdDraw.setPenColor(StdDraw.BLACK);
                                StdDraw.setFont(new Font("angryBulletsGame", Font.BOLD, 20));
                                StdDraw.textLeft(10, 780, "Hit an obstacle. Press 'r' to shoot again.");
                                StdDraw.show();
                                gameRunning = false;
                                break;
                            }

                            if (targetHit) { // If targetHit returns true above, this function will write "Congratulations: You hit the target!" to the left top corner.
                                StdDraw.setPenColor(StdDraw.BLACK);
                                StdDraw.setFont(new Font("angryBulletsGame", Font.BOLD, 20));
                                StdDraw.textLeft(10, 780, "Congratulations: You hit the target!");
                                StdDraw.show();
                                gameRunning = false;
                                break;
                            }

                            if (x1 >= 1600) { // If x location of bullet exceeds our canvas width, his function will write "Max X reached. Press 'r' to shoot again." to the left top corner.
                                StdDraw.setPenColor(StdDraw.BLACK);
                                StdDraw.setFont(new Font("angryBulletsGame", Font.BOLD, 20));
                                StdDraw.textLeft(10, 780, "Max X reached. Press 'r' to shoot again.");
                                StdDraw.show();
                                gameRunning = false;
                                break;
                            }

                            if (y1 <= 0) { // If y location of bullet exceeds our canvas height this function will write "Hit the ground. Press 'r' to shoot again." to the left top corner.
                                StdDraw.setPenColor(StdDraw.BLACK);
                                StdDraw.setFont(new Font("angryBulletsGame", Font.BOLD, 20));
                                StdDraw.textLeft(10, 780, "Hit the ground. Press 'r' to shoot again.");
                                StdDraw.show();
                                gameRunning = false;
                                break;
                            }
                        }
                    }
                } else {
                    if (StdDraw.isKeyPressed(KeyEvent.VK_R)) { // If we press R button, gameRunning will return true and game loop will be start again with its initial velocity and angle.
                        gameRunning = true;
                        bulletVelocity = 180;
                        bulletAngle = 45.0;
                    }
                }
                StdDraw.pause(70); // Adjusts the speed of the animation.
                StdDraw.show(); // Shows the animation.
            }
        }
    }
