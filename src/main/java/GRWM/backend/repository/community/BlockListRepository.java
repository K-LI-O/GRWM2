package GRWM.backend.repository.community;

import GRWM.backend.entity.community.BlockList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlockListRepository extends JpaRepository<BlockList, Long> {

}
