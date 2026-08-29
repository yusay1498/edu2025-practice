import Select from "react-select";
import PropTypes from "prop-types";

const SelectCustomer = ({ customers, onSelect }) => {
  const options = customers.map((c) => ({
    value: c.customerId,
    label: c.customerId,
  }));

  return (
    <Select
      instanceId="search-select-box"
      options={options}
      onChange={(option) => onSelect(option)}
      noOptionsMessage={() => "IDが見つかりません"}
      placeholder="IDを入力してください"
      isSearchable
      className="custom-select"
      classNamePrefix="custom-select"
      components={{
        IndicatorSeparator: () => null,
      }}
    />
  );
};

SelectCustomer.propTypes = {
  customers: PropTypes.arrayOf(
    PropTypes.shape({
      customerId: PropTypes.string.isRequired,
    })
  ).isRequired,
  onSelect: PropTypes.func.isRequired,
};

export default SelectCustomer;
