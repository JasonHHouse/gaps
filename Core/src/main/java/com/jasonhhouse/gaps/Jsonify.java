package com.jasonhhouse.gaps;

import com.fasterxml.jackson.databind.node.ObjectNode;

public interface Jsonify<T> {

    ObjectNode toJSON();

}
