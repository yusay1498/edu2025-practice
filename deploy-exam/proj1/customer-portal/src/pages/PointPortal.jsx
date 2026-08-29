import React from 'react';
import { useParams } from 'react-router-dom';
import { useFetch } from '../hooks/useFetch';
import { fetchCustomerDetail } from '../api/pointApi';

const PointPortal = () => {
  const { customerId } = useParams();
  const { data: customerDetail, loading, error } = useFetch(
    () => fetchCustomerDetail(customerId),
    [customerId]
  );

  if (loading) {
    return <div className="loading">読み込み中…</div>;
  }
  if (error) {
    return <div className="error">エラー: {error.message}</div>;
  }
  if (!customerDetail) {
    return <div>データなし</div>;
  }

  return (
    <div className="wrap">
      <div className="customer">
        <div className="mitsukoshi">
          <h1 className="mitsukoshi_name">&nbsp;</h1>
          <h2 className="mitsukoshi_member">GROUP&nbsp;&nbsp;MEMBER</h2>
        </div>
        <p className="customer_id">{customerDetail.customerId}</p>
        <div className="customer_point">
          <p>エムアイポイント</p>
          <div className="customer_point_num">
            <p className="customer_point_num_main">{customerDetail.currentPoints}</p>
            <p className="customer_point_num_unit">ポイント</p>
          </div>
        </div>
      </div>
    </div>
  );
};

export default PointPortal;
