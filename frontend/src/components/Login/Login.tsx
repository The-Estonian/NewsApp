import { useState } from 'react';

import type { UserData } from '../../types';

import { loginUser } from '../connection/backend';
import Loader from '../Loader/Loader';
import styles from './Login.module.css';

type LoginProps = {
  setAuthenticated: () => void;
  setHideLogin: (b: boolean) => void;
  setUserMessage: (message: string) => void;
  setUserData: React.Dispatch<React.SetStateAction<UserData | null>>;
};

const Login = ({
  setAuthenticated,
  setHideLogin,
  setUserMessage,
  setUserData,
}: LoginProps) => {
  const [loading, setLoading] = useState(false);
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [showError, setShowError] = useState(false);
  const [error, setError] = useState('');

  const usernameHandler = (e: React.ChangeEvent<HTMLInputElement>) => {
    setUsername(e.target.value);
  };

  const passwordHandler = (e: React.ChangeEvent<HTMLInputElement>) => {
    setPassword(e.target.value);
  };

  const handleLogin = async () => {
    setLoading(true);
    setHideLogin(false);
    setShowError(false);
    setUserMessage('');
    try {
      const response = await loginUser({
        username,
        password,
      });

      const data = await response.json();
      setUserData({
        username: data.username,
        email: data.email,
      });
      if (!response.ok || (data.status && data.status !== 200)) {
        setError(data.message || 'Login failed');
        setShowError(true);
        setLoading(false);
        setHideLogin(true);
        setUserMessage('');
        return;
      }
      setAuthenticated();
    } catch (error: any) {
      setError(error.message || 'Unexpected error');
      setShowError(true);
      setHideLogin(false);
    } finally {
      setLoading(false);
    }
  };
  return (
    <div className={styles.loginContainer}>
      {!loading && (
        <div className={styles.loginInput}>
          <span>Username</span>
          <input type='text' value={username} onChange={usernameHandler} />
          <span>Password</span>
          <input type='password' value={password} onChange={passwordHandler} />
          <button onClick={handleLogin}>Login</button>
        </div>
      )}
      {showError && <p className={styles.error}>{error}</p>}
      {loading && <Loader />}
    </div>
  );
};

export default Login;
