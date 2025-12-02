import styles from './Profile.module.css';

import type { UserData } from '../../types';

type ProfileProps = {
  userData: UserData | null;
};

const Profile = ({ userData }: ProfileProps) => {
  return (
    <div className={styles.profileContainer}>
      <span>Profile</span>
      <span>Username: {userData?.username}</span>
      <span>email: {userData?.email}</span>
    </div>
  );
};

export default Profile;
