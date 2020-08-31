/*
 * Copyright 2020 Jason H House
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.jasonhhouse.gaps.service;

import com.jasonhhouse.gaps.Mislabeled;
import com.jasonhhouse.plex.video.MediaContainer;
import com.jasonhhouse.plex.video.Video;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class MislabeledService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MislabeledService.class);
    private static final LevenshteinDistance levenshteinDistance = new LevenshteinDistance();

    public List<Mislabeled> findMatchPercentage(MediaContainer mediaContainer, Double percentage) {
        LOGGER.info("findMatchPercentage( {} )", percentage);
        List<Mislabeled> mislabeled = new ArrayList<>();
        for (Video video : mediaContainer.getVideos()) {
            String file = video.getMedia().get(0).getParts().get(0).getFile();
            if (file.contains("(")) {
                file = file.substring(0, file.lastIndexOf('(') - 1);
            } else {
                file = file.substring(0, file.lastIndexOf('.') - 1);
            }
            file = file.substring(file.lastIndexOf('/') + 1);
            String title = video.getTitle();
            title = title.replace(":", "");
            Double matchPercentage = similarity(title, file);
            if (matchPercentage < percentage) {
                Mislabeled mislabeledItem = new Mislabeled(file, title, matchPercentage);
                mislabeled.add(mislabeledItem);
            }
        }
        return mislabeled;
    }

    private Double similarity(String s1, String s2) {
        String longer = s1.toLowerCase();
        String shorter = s2.toLowerCase();
        if (s1.length() < s2.length()) { // longer should always have greater length
            longer = s2;
            shorter = s1;
        }
        int longerLength = longer.length();
        if (longerLength == 0) {
            return 1.0; /* both strings are zero length */
        }

        return (longerLength - levenshteinDistance.apply(longer, shorter)) / (double) longerLength;
    }

}
