package numble.jjan.reply.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import numble.jjan.reply.entity.Reply;

public interface ReplyRepository extends JpaRepository<Reply, Long>  {
	Reply findByPostId(String postId);
}
