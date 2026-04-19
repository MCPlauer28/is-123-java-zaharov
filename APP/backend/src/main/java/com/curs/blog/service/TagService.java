package com.curs.blog.service;

import com.curs.blog.entity.Tag;
import java.util.List;
import java.util.Optional;

public interface TagService {
    List<Tag> getAllTags();
    Optional<Tag> getTagByName(String name);
    Tag saveTag(Tag tag);
}
