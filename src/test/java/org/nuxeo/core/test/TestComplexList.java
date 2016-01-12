/*
 * (C) Copyright 2016 Nuxeo SA (http://nuxeo.com/) and others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributors:
 *     vdutat
 */
package org.nuxeo.core.test;

import static org.junit.Assert.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.test.annotations.Granularity;
import org.nuxeo.ecm.core.test.annotations.RepositoryConfig;
import org.nuxeo.ecm.platform.test.PlatformFeature;
import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;

@RunWith(FeaturesRunner.class)
@Features({ PlatformFeature.class })
@RepositoryConfig(cleanup = Granularity.METHOD)
@Deploy({
    "nuxeo-schema-complex-list-test"
})
public class TestComplexList {

    private static final String COMPLEX_OBJECT_ELEMENT_PROPNAME = "stringlist";

    private static final String COMPLEX_LIST_PROPNAME = "supnxp-15576:complexList";

    private static final String DOCTYPE = "SUPNXP-15576";

    @Inject
    protected CoreSession session;

    @Test
    public void testResetList() {
        DocumentModel doc = session.createDocumentModel(DOCTYPE);
        assertNotNull(doc);
        doc.setPathInfo("/", "mydoc");
        List<Map<String, Serializable>> complexListProp = new ArrayList<>();
        HashMap<String, Serializable> complexListElem = new HashMap<>();
        complexListElem.put(COMPLEX_OBJECT_ELEMENT_PROPNAME, new String[] {"string1"});
        complexListProp.add(complexListElem);
        doc.setPropertyValue(COMPLEX_LIST_PROPNAME, (Serializable) complexListProp);
        doc = session.createDocument(doc);
        session.save();
        assertNotNull(doc);
        List<Map<String, Serializable>> propertyValue = (List<Map<String, Serializable>>) doc.getPropertyValue(COMPLEX_LIST_PROPNAME);
        Map<String, Serializable> map = propertyValue.get(0);
        assertEquals(1, ((String[]) map.get(COMPLEX_OBJECT_ELEMENT_PROPNAME)).length);
        assertEquals("string1", ((String[]) map.get(COMPLEX_OBJECT_ELEMENT_PROPNAME))[0]);

        // set 'stringlist' to null
        map.put(COMPLEX_OBJECT_ELEMENT_PROPNAME, null);
//        map.replace("stringlist", null);
//        map.replace("stringlist", new String[] {});
        propertyValue.set(0, map);
        doc.setPropertyValue(COMPLEX_LIST_PROPNAME, (Serializable) propertyValue);
        doc = session.saveDocument(doc);
        session.save();
        assertNotNull(doc);
        List<Map<String, Serializable>> propertyValue2 = (List<Map<String, Serializable>>) doc.getPropertyValue(COMPLEX_LIST_PROPNAME);
        Map<String, Serializable> map2 = propertyValue2.get(0);
        assertEquals("'stringlist' should be empty,", 0, ((String[]) map2.get(COMPLEX_OBJECT_ELEMENT_PROPNAME)).length);


    }

}
