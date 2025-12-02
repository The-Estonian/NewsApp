import { useEffect, useState } from 'react';
import Login from './components/Login/Login';
import Posts from './components/Posts/Posts';
import { status, logOut } from './components/connection/backend';

import styles from './App.module.css';
import Register from './components/Register/Register';

function App() {
  const [loggedIn, setLoggedIn] = useState(false);
  const [loginOrRegister, setLoginOrRegister] = useState(true);
  const [hideLogin, setHideLogin] = useState(true);
  const [userMessage, setUserMessage] = useState('');

  useEffect(() => {
    const checkStatus = async () => {
      const response = await status();
      const data = await response.json();
      if (data.authenticated == true) {
        setLoggedIn(true);
      } else {
        setLoggedIn(false);
      }
    };

    checkStatus();
  }, []);

  const setAuthenticated = () => {
    setLoggedIn(!loggedIn);
  };

  const handleLogOut = () => {
    logOut();
    setHideLogin(true);
    setAuthenticated();
  };

  const handleLoginOrRegister = () => {
    setLoginOrRegister(!loginOrRegister);
  };


  return (
    <div className={styles.container}>
      {!loggedIn && loginOrRegister && (
        <Login
          setAuthenticated={setAuthenticated}
          setHideLogin={setHideLogin}
          setUserMessage={setUserMessage}
        />
      )}
      {!loggedIn && !loginOrRegister && (
        <Register
          handleLoginOrRegister={handleLoginOrRegister}
          setHideLogin={setHideLogin}
          setUserMessage={setUserMessage}
        />
      )}
      {userMessage ? <p className={styles.message}>{userMessage}</p> : ''}
      {!loggedIn &&
        hideLogin &&
        (loginOrRegister ? (
          <p className={styles.loginRegister} onClick={handleLoginOrRegister}>
            Don't have an account, register here!
          </p>
        ) : (
          <p className={styles.loginRegister} onClick={handleLoginOrRegister}>
            Already have an account? Login here!
          </p>
        ))}
      {loggedIn && <Posts />}
      {loggedIn && (
        <span className={styles.logout} onClick={handleLogOut}>
          Logout
        </span>
      )}
    </div>
  );
}

export default App;
