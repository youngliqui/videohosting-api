package ru.clevertec.videohosting_api.model;

import org.springframework.data.jpa.domain.Specification;

public class ChannelSpecification {
    public static Specification<Channel> hasName(String channelName) {
        return (root, query, criteriaBuilder) -> {
            if (channelName == null || channelName.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(root.get("name"), "%" + channelName + "%");
        };
    }

    public static Specification<Channel> hasLanguage(String language) {
        return (root, query, criteriaBuilder) -> {
            if (language == null || language.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(root.get("language"), language);
        };
    }

    public static Specification<Channel> hasCategory(String categoryName) {
        return (root, query, criteriaBuilder) -> {
            if (categoryName == null || categoryName.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(root.get("category").get("name"), categoryName);
        };
    }
}
