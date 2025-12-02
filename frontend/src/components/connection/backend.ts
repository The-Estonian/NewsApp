const API_URL = import.meta.env.VITE_API_URL || '/';

// Authentication
export const status = async () =>
  await fetch(`${API_URL}api/v1/status`, {
    method: 'GET',
    credentials: 'include',
  });

export const logOut = async () =>
  await fetch(`${API_URL}api/v1/logout`, {
    method: 'GET',
    credentials: 'include',
  });

export const registerUser = async (data: { [key: string]: any }) =>
  await fetch(`${API_URL}api/v1/register`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(data),
    credentials: 'include',
  });

export const loginUser = async (data: Record<string, any>) =>
  await fetch(`${API_URL}api/v1/login`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(data),
    credentials: 'include',
  });

// Posts
export const newPost = async (data: { [key: string]: any }) =>
  await fetch(`${API_URL}api/v1/posts/add`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(data),
    credentials: 'include',
  });

export const getPosts = async () =>
  await fetch(`${API_URL}api/v1/posts`, {
    method: 'GET',
    credentials: 'include',
  });

export const deletePost = async (id: string) =>
  await fetch(`${API_URL}api/v1/posts/${id}`, {
    method: 'DELETE',
    credentials: 'include',
  });
