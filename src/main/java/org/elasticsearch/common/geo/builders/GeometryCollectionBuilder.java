/*
 * Licensed to Elasticsearch under one or more contributor
 * license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright
 * ownership. Elasticsearch licenses this file to you under
 * the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.elasticsearch.common.geo.builders;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.spatial4j.core.shape.ShapeCollection;
import org.elasticsearch.common.xcontent.XContentBuilder;

import com.spatial4j.core.shape.Shape;

public class GeometryCollectionBuilder extends ShapeBuilder {

    public static final String FIELD_GEOMETRIES = "geometries";
    public static final GeoShapeType TYPE = GeoShapeType.GEOMETRYCOLLECTION;

    protected final ArrayList<ShapeBuilder> geometries = new ArrayList<>();

    public GeometryCollectionBuilder geometry(ShapeBuilder geometry) {
        this.geometries.add(geometry);
        return this;
    }

    @Override
    public XContentBuilder toXContent(XContentBuilder builder, Params params) throws IOException {
        builder.startObject();
        builder.field(FIELD_TYPE, TYPE.shapename);
        builder.startArray(FIELD_GEOMETRIES);
        for(ShapeBuilder geometry : geometries) {
            geometry.toXContent(builder,params);
        }
        builder.endArray();
        return builder.endObject();
    }

    @Override
    public GeoShapeType type() {
        return TYPE;
    }

    @Override
    public Shape build() {
        List<Shape> shapes = new ArrayList<>(this.geometries.size());
        for (ShapeBuilder geometry : this.geometries) {
            shapes.add(geometry.build());
        }
        return new ShapeCollection<>(shapes, SPATIAL_CONTEXT);
    }
}
