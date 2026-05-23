package gt.edu.miumg.app.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;
import java.util.Optional;

public interface MongoNodeRepository extends MongoRepository<NodeDocument, String> {

    Optional<NodeDocument> findByParentIdIsNull();

    List<NodeDocument> findByParentId(String parentId);
}