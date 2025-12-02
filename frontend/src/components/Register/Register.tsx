import { useState } from 'react';

import { registerUser } from '../connection/backend';
import Loader from '../Loader/Loader';
import styles from './Register.module.css';

type RegisterProps = {
  handleLoginOrRegister: () => void;
  setHideLogin: (b: boolean) => void;
  setUserMessage: (message: string) => void;
};

const Register = ({
  handleLoginOrRegister,
  setHideLogin,
  setUserMessage,
}: RegisterProps) => {
  const [loading, setLoading] = useState(false);
  const [username, setUsername] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [showError, setShowError] = useState(false);
  const [error, setError] = useState('');

  const usernameHandler = (e: React.ChangeEvent<HTMLInputElement>) => {
    setUsername(e.target.value);
  };

  const emailHandler = (e: React.ChangeEvent<HTMLInputElement>) => {
    setEmail(e.target.value);
  };

  const passwordHandler = (e: React.ChangeEvent<HTMLInputElement>) => {
    setPassword(e.target.value);
  };

  const handleRegister = async () => {
    setLoading(true);
    setHideLogin(false);
    setShowError(false);
    setUserMessage('');
    try {
      const response = await registerUser({
        username,
        password,
        email,
      });

      const data = await response.json();
      setUserMessage(data.message + '! Please login');

      if (!response.ok || (data.status && data.status !== 200)) {
        setError(data.message || 'Register failed');
        setShowError(true);
        setLoading(false);
        setHideLogin(true);
        setUserMessage('');
        return;
      }
      handleLoginOrRegister();
      setHideLogin(true);
    } catch (error: any) {
      setError(error.message || 'Unexpected error');
      setShowError(true);
    } finally {
      setLoading(false);
    }
  };
  return (
    <div className={styles.registerContainer}>
      {!loading && (
        <div className={styles.registerInput}>
          <span>Username</span>
          <input type='text' value={username} onChange={usernameHandler} />
          <span>Email</span>
          <input type='email' value={email} onChange={emailHandler} />
          <span>Password</span>
          <input type='password' value={password} onChange={passwordHandler} />
          <button onClick={handleRegister}>Register</button>
        </div>
      )}
      {showError && <p className={styles.error}>{error}</p>}
      {loading && <Loader />}
    </div>
  );
};

export default Register;
