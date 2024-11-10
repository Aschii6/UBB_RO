import { useEffect } from 'react';
import { Preferences } from '@capacitor/preferences';

export const usePreferences = () => {
  useEffect(() => {
    runPreferencesDemo();
  }, []);

  function runPreferencesDemo() {
    (async () => {
      
      await Preferences.set({
        key: 'user',
        value: JSON.stringify({
          username: 'a', password: 'a',
        })
      });

      
      const res = await Preferences.get({ key: 'user' });
      if (res.value) {
        console.log('User found', JSON.parse(res.value));
      } else {
        console.log('User not found');
      }

      
      const { keys } = await Preferences.keys();
      console.log('Keys found', keys);

      
      await Preferences.remove({ key: 'user' });
      console.log('Keys found after remove', await Preferences.keys());

      
      await Preferences.clear();
    })();
  }
};
