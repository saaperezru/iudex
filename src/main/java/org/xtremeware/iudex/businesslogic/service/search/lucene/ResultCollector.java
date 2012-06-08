package org.xtremeware.iudex.businesslogic.service.search.lucene;

import java.io.IOException;
import java.util.Set;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.Collector;
import org.apache.lucene.search.Scorer;

/**
 *
 * @author josebermeo
 */
public class ResultCollector extends Collector{

    private final Set<Integer> bag;

    public ResultCollector(Set<Integer> bag) {
        this.bag = bag;
    }
    
    private int base = 0;

    @Override
    public void setScorer(Scorer scorer) throws IOException {
    }

    @Override
    public void collect(int doc) {
        bag.add(Integer.valueOf(doc + base));
    }

    @Override
    public void setNextReader(IndexReader reader, int docBase) {
        base = docBase;
    }

    @Override
    public boolean acceptsDocsOutOfOrder() {
        return true;
    }

    public Set<Integer> getBag() {
        return bag;
    }

    public int getBase() {
        return base;
    }
}
