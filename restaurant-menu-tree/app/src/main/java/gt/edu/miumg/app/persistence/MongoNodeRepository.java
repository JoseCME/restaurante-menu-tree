package gt.edu.miumg.app.persistence;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;
import java.util.Optional;

@ConditionalOnProperty(name = "app.storage", havingValue = "mongo")
public interface MongoNodeRepository extends MongoRepository<NodeDocument, String> {
    Optional<NodeDocument> findByParentIdIsNull();
    List<NodeDocument> findByParentId(String parentId);
}
