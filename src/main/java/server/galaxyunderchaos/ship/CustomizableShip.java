package server.galaxyunderchaos.ship;

public interface CustomizableShip {
    int getShipColor(ShipColorSection section);

    void setShipColor(ShipColorSection section, int color);
}
