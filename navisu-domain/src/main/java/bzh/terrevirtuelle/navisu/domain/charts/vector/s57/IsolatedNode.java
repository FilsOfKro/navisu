package bzh.terrevirtuelle.navisu.domain.charts.vector.s57;

import bzh.terrevirtuelle.navisu.domain.charts.vector.s57.controler.analyzer.DataSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public class IsolatedNode extends Node {

    /**
     * Enregistrements spatiaux associ�s � l'objet rep�r�s par leurs
     * identifiants
     */
    protected HashMap<Long, VectorUsage> spatialRecordById;
    /**
     * Enregistrements spatiaux associ�s � l'objet
     */
    protected HashMap<Spatial, VectorUsage> spatialRecord;

    public IsolatedNode(long rcid) {
        this.spatialRecord = new HashMap<>();
        this.spatialRecordById = new HashMap<>(2);
        this.setId(rcid);
    }

    public IsolatedNode() {
        this.spatialRecord = new HashMap<>();
        this.spatialRecordById = new HashMap<>(2);
    }

    public int cardSpatialRecordById() {
        return this.spatialRecordById.size();
    }

    /**
     * R�cup�re les noeuds de d�but et de fin, les faces de droite et de gauche
     */
    @Override
    public void decodVRPT(byte[] fieldValue) {
        for (int i = 0; i < fieldValue.length; i += 9) {
            long id0;
            id0 = fieldValue[i] & 0xFF;
            id0 = id0 * 256 + (fieldValue[i + 4] & 0xFF);
            id0 = id0 * 256 + (fieldValue[i + 3] & 0xFF);
            id0 = id0 * 256 + (fieldValue[i + 2] & 0xFF);
            id0 = id0 * 256 + (fieldValue[i + 1] & 0xFF);

            spatialRecordById.put(id0, new VectorUsage(fieldValue[i + 5], fieldValue[i + 6], fieldValue[i + 7], fieldValue[i + 8], 0));
        }
    }

    @Override
    public void linkObjects() {
        if (this.cardSpatialRecordById() != 0) {
            HashSet<Long> spatials = new HashSet<>(spatialRecordById.keySet());
            Iterator<Long> it = spatials.iterator();
            while (it.hasNext()) {
                Long id = it.next();
                spatialRecord.put(DataSet.getSpatialObject(id), this.spatialRecordById.get(id));
            }
        }
    }

    public HashMap<Spatial, VectorUsage> getSpatialRecord() {
        return spatialRecord;
    }

    @Override
    public String toString() {
        return super.toString() + "  IsolatedNode{" + "spatialRecordById=" + spatialRecordById + ", spatialRecord=" + spatialRecord + '}';
    }

}
