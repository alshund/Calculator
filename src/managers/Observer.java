package managers;

import model.graph.Node;

/**
 * Created by shund on 14.06.2017.
 */
public interface Observer {
    void update(Node currentNode, int depth);
    void setInputText(String inputText);
    void removeData();
}
