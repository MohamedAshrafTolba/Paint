package controller.SubController;

import java.util.ArrayList;
import java.util.List;

public class SavePane {

    public List<String> paneShapes = new ArrayList<String>();

    SavePane(List<String> allShapes) {
	// TODO Auto-generated constructor stub
	for (int i = 0; i < allShapes.size(); i++)
	    paneShapes.add(allShapes.get(i));

    }

}
