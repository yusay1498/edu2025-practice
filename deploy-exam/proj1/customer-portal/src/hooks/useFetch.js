import { useState, useEffect } from 'react';

export function useFetch(fetcher, deps = []) {
  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    let canceled = false;
    async function doFetch() {
      setLoading(true);
      setError(null);
      try {
        const result = await fetcher();
        if (!canceled) {
          setData(result);
        }
      } catch (err) {
        if (!canceled) {
          setError(err);
        }
      } finally {
        if (!canceled) {
          setLoading(false);
        }
      }
    }
    doFetch();
    return () => {
      canceled = true;
    };
  }, deps);

  return { data, loading, error };
}
