package engine.graphics;


import engine.EngineUtils;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class OBJLoader {

    public static Mesh loadMesh(String fileName) {

        String meshName;

        List<String> lines = EngineUtils.readAllLines(fileName);

        List<Vector3f> vertices = new ArrayList<>();
        List<Vector2f> textures = new ArrayList<>();
        List<Vector3f> normals = new ArrayList<>();
        List<Face> faces = new ArrayList<>();

        for (String line : lines) {

            String[] tokens = line.split("\\s+");

            switch (tokens[0]) {
                case "o" :
                    meshName = tokens[1];
                    break;

                // Geometric vertex
                case "v" :
                    Vector3f vec3f = new Vector3f(
                            Float.parseFloat(tokens[1]),
                            Float.parseFloat(tokens[2]),
                            Float.parseFloat(tokens[3]));
                    vertices.add(vec3f);
                    break;

                // Texture coordinate
                case "vt" :
                    Vector2f vec2f = new Vector2f(
                            Float.parseFloat(tokens[1]),
                            Float.parseFloat(tokens[2]));
                    textures.add(vec2f);
                    break;

                // Vertex normal
                case "vn" :
                    Vector3f vec3fNorm = new Vector3f(
                            Float.parseFloat(tokens[1]),
                            Float.parseFloat(tokens[2]),
                            Float.parseFloat(tokens[3]));
                    normals.add(vec3fNorm);
                    break;

                // Face
                case "f":
                    Face face = new Face(tokens[1], tokens[2], tokens[3]);
                    faces.add(face);
                    break;

                default:
                    // Ignore other lines
                    break;
            }
        }

        return reorderLists(vertices, textures, normals, faces);
    }

    private static Mesh reorderLists(List<Vector3f> posList, List<Vector2f> textCoordList, List<Vector3f> normList, List<Face> facesList) {

        float[] posArr = new float[posList.size() * 3];
        float[] textCoordArr = new float[posList.size() * 2];
        float[] normArr = new float[posList.size() * 3];
        List<Integer> indicesList = new ArrayList<>();

        int i = 0;
        for (Vector3f pos : posList) {
            posArr[i * 3] = pos.x;
            posArr[i * 3 + 1] = pos.y;
            posArr[i * 3 + 2] = pos.z;
            i++;
        }

        for (Face face : facesList) {
            for (IndexGroup indices : face.getIndexGroups()) {

                int posIndex = indices.idxPos;
                indicesList.add(posIndex);

                // Reorder texture coordinates
                if (indices.idxTextCoord >= 0) {
                    Vector2f textCoord = textCoordList.get(indices.idxTextCoord);
                    textCoordArr[posIndex * 2] = textCoord.x;
                    textCoordArr[posIndex * 2 + 1] = 1 - textCoord.y;
                }

                // Reorder vectornormals
                if (indices.idxVecNormal >= 0) {
                    Vector3f vecNormal = normList.get(indices.idxVecNormal);
                    normArr[posIndex * 3] = vecNormal.x;
                    normArr[posIndex * 3 + 1] = vecNormal.y;
                    normArr[posIndex * 3 + 2] = vecNormal.z;
                }
            }
        }

        int[] indicesArr = indicesList.stream().mapToInt((Integer v) -> v).toArray();
        return new Mesh(posArr, textCoordArr, normArr, indicesArr);
    }

    private static class Face {

        // List of idxGroup groups for a face triangle (3 vertices per face).
        private IndexGroup[] indexGroups = new IndexGroup[3];

        public Face(String v1, String v2, String v3) {
            indexGroups[0] = parseLine(v1);
            indexGroups[1] = parseLine(v2);
            indexGroups[2] = parseLine(v3);
        }

        private IndexGroup parseLine(String line) {

            IndexGroup group = new IndexGroup();

            String[] lineTokens = line.split("/");

            group.idxPos = Integer.parseInt(lineTokens[0]) - 1;
            if (lineTokens.length > 1) {
                // It can be empty if the obj does not define text coords
                String textCoord = lineTokens[1];
                if (!textCoord.isEmpty()) {
                    group.idxTextCoord = Integer.parseInt(textCoord) - 1;
                }

                if (lineTokens.length > 2) {
                    group.idxVecNormal = Integer.parseInt(lineTokens[2]) - 1;
                }
            }

            return group;
        }

        public IndexGroup[] getIndexGroups() {
            return indexGroups;
        }
    }

    protected static class IndexGroup {

        public static final int NO_VALUE = -1;

        public int idxPos = NO_VALUE;
        public int idxTextCoord = NO_VALUE;
        public int idxVecNormal = NO_VALUE;
    }
}
