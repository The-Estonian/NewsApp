import { useEffect, useState } from 'react';
import {
  getPosts,
  deletePost,
  newPost,
  updatePost,
} from '../connection/backend';
import Loader from '../Loader/Loader';

import styles from './Posts.module.css';

const Posts = () => {
  const [posts, setPosts] = useState([]);
  const [newTitleToSend, setNewTitleToSend] = useState('');
  const [newPostToSend, setNewPostToSend] = useState('');
  const [updatePostId, setUpdatePostId] = useState('');
  const [loading, setLoading] = useState(false);
  const [showError, setShowError] = useState(false);
  const [error, setError] = useState('');
  const [showUpdate, setShowUpdate] = useState(true);

  const fetchPosts = async () => {
    const response = await getPosts();
    const data = await response.json();

    if (!response.ok || (data.status && data.status !== 200)) {
      setError(data.message || 'Failed fetching posts');
      setShowError(true);
      setLoading(false);
      return;
    }
    setPosts(data);
  };

  useEffect(() => {
    fetchPosts();
  }, []);

  const handleDelete = async (id: string) => {
    const response = await deletePost(id);
    if (response.ok) {
      fetchPosts();
    }
  };

  const handleNewTitleInput = (e: React.ChangeEvent<HTMLInputElement>) => {
    setNewTitleToSend(e.target.value);
  };
  const handleNewPostInput = (e: React.ChangeEvent<HTMLInputElement>) => {
    setNewPostToSend(e.target.value);
  };

  const handleNewPost = async () => {
    const newData = {
      title: newTitleToSend,
      post: newPostToSend,
    };
    const response = await newPost(newData);
    if (response.ok) {
      setNewTitleToSend('');
      setNewPostToSend('');
      fetchPosts();
    }
  };

  const handleEdit = (post: { id: string; title: string; post: string }) => {
    setShowUpdate(false);
    setUpdatePostId(post.id);
    setNewTitleToSend(post.title);
    setNewPostToSend(post.post);
  };

  const handleUpdatePost = async () => {
    const newData = {
      title: newTitleToSend,
      post: newPostToSend,
    };
    const response = await updatePost(newData, updatePostId);
    console.log(response);

    if (response.ok) {
      setUpdatePostId('');
      setNewTitleToSend('');
      setNewPostToSend('');
      setShowUpdate(true);
      fetchPosts();
    }
  };

  const cancelEdit = () => {
    setShowUpdate(true);
    setNewTitleToSend('');
    setNewPostToSend('');
  };

  return (
    <div className={styles.postsContainer}>
      {showError && <p>{error}</p>}
      {loading && <Loader />}
      {!loading &&
        !showError &&
        posts.map((eachPost: { id: string; title: string; post: string }) => (
          <div key={eachPost.id} className={styles.post}>
            <span className={styles.post_title}>{eachPost.title}</span>
            <span className={styles.post_post}>{eachPost.post}</span>
            <div className={styles.editPost}>
              <span onClick={() => handleEdit(eachPost)}>Edit</span>
            </div>
            <div className={styles.deletePost}>
              <span onClick={() => handleDelete(eachPost.id)}>X</span>
            </div>
          </div>
        ))}
      {!loading && !showError && (
        <div className={styles.newPost}>
          <span>Title</span>
          <input
            onChange={handleNewTitleInput}
            type='text'
            value={newTitleToSend}
          />
          <span>Post</span>
          <input
            onChange={handleNewPostInput}
            type='text'
            value={newPostToSend}
          />
          {showUpdate ? (
            <span className={styles.sendPost} onClick={handleNewPost}>
              Send
            </span>
          ) : (
            <div className={styles.editPostOptions}>
              <span className={styles.sendPost} onClick={handleUpdatePost}>
                Update
              </span>
              <span className={styles.sendPost} onClick={cancelEdit}>
                Cancel
              </span>
            </div>
          )}
        </div>
      )}
    </div>
  );
};

export default Posts;
