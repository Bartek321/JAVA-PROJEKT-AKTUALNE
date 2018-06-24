package Pack;

import java.awt.Component;
import java.awt.Container;
import java.util.ArrayList;
import java.util.List;

public class Util {
	
	public static List<Component> getAllComponents(final Container c) {
        Component[] comps = c.getComponents();
        List<Component> compList = new ArrayList<Component>();
        for (Component comp : comps) {
            compList.add(comp);
            if (comp instanceof Container) {
                compList.addAll(getAllComponents((Container) comp));
            }
        }
        return compList;
      }
    
    public static Component getComponent(String name, List<Component> list) {
        for (Component i : list) {
        	if(i.getName() != null) {
	        	if(i.getName().equals(name))
	        		return i;
        	}
        }
        	return null;
    }

}
