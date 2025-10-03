package com.newsroom.commons;

public class Constants {
    public static final class ERROR {
        public static final class REQUEST {
            public static final String INVALID_PATH_VARIABLE
                    = "error.request.invalid_path_variable";
            public static final String INVALID_PATH_VARIABLE_ID
                    = "error.request.invalid_path_variable_id";
            public static final String INVALID_PATH_VARIABLE_CODE =
                    "error.request.invalid.path_variable.code";
            public static final String INVALID_PATH_VARIABLE_EMAIL =
                    "error.request.invalid.path_variable.email";
            public static final String INVALID_PATH_VARIABLE_STATUS =
                    "error.request.invalid.path_variable.status";
            public static final String INVALID_PATH_VARIABLE_STATE =
                    "error.request.invalid.path_variable.state";
            public static final String INVALID_BODY = "error.request.body.invalid";
        }

        public static final class CUSTOMER {
            public static final String CREATE = "error.customer.create";
            public static final String UPDATE = "error.customer.update";
            public static final String UPDATE_STATE = "error.customer.update_state";
            public static final String UPDATE_STATUS = "error.customer.update_status";
            public static final String EXIST = "error.customer.exist";
            public static final String NOT_EXIST = "error.customer.not_exist";
            public static final String CODE_BLANK = "error.customer.code_blank";
            public static final String IDENTIFICATION_BLANK = "error.customer.identification_blank";
            public static final String INVALID_EMAIL = "error.customer.invalid_email";
        }

        public static final class CATEGORY {
            public static final String CREATE = "error.category.create";
            public static final String UPDATE = "error.category.update";
            public static final String UPDATE_STATE = "error.category.update_state";
            public static final String UPDATE_STATUS = "error.category.update_status";
            public static final String EXIST = "error.category.exist";
            public static final String EXIST_INDEX = "error.category.exist.index";
            public static final String NOT_EXIST = "error.category.not_exist";
            public static final String CODE_BLANK = "error.category.code_blank";
            public static final String IDENTIFICATION_BLANK = "error.category.identification_blank";
        }

        public static final class ORDER {
            public static final String CREATE = "error.order.create";
            public static final String UPDATE = "error.order.update";
            public static final String UPDATE_STATE = "error.order.update_state";
            public static final String UPDATE_STATUS = "error.order.update_status";
            public static final String EXIST = "error.order.exist";
            public static final String EXIST_PRODUCT_IN_ORDER_DETAIL = "error.exist_product_in_order_detail";
            public static final String NOT_EXIST = "error.order.not_exist";
            public static final String CODE_BLANK = "error.order.code_blank";
            public static final String IDENTIFICATION_BLANK = "error.order.identification_blank";
        }
        public static final class PRODUCT {
            public static final String CREATE = "error.product.create";
            public static final String UPDATE = "error.product.update";
            public static final String UPDATE_STATE = "error.product.update_state";
            public static final String UPDATE_STATUS = "error.product.update_status";
            public static final String EXIST = "error.product.exist";
            public static final String NOT_EXIST = "error.product.not_exist";
            public static final String CODE_BLANK = "error.product.code_blank";
            public static final String IDENTIFICATION_BLANK = "error.product.identification_blank";
        }
        public static final class TABLE {
            public static final String CREATE = "error.table.create";
            public static final String UPDATE = "error.table.update";
            public static final String UPDATE_STATE = "error.table.update_state";
            public static final String UPDATE_STATUS = "error.table.update_status";
            public static final String EXIST = "error.table.exist";
            public static final String NOT_EXIST = "error.table.not_exist";
            public static final String CODE_BLANK = "error.table.code_blank";
            public static final String IDENTIFICATION_BLANK = "error.table.identification_blank";
        }
        public static final class USER {
            public static final String CREATE = "error.user.create";
            public static final String UPDATE = "error.user.update";
            public static final String UPDATE_STATE = "error.user.update_state";
            public static final String UPDATE_STATUS = "error.user.update_status";
            public static final String EXIST = "error.user.exist";
            public static final String NOT_EXIST = "error.user.not_exist";
            public static final String INVALID_CREDENTIAL = "error.user.invalid_credential";
            public static final String CODE_BLANK = "error.user.code_blank";
            public static final String IDENTIFICATION_BLANK = "error.user.identification_blank";
        }

        public static final class ROLE {
            public static final String CREATE = "error.role.create";
            public static final String UPDATE = "error.role.update";
            public static final String UPDATE_STATE = "error.role.update_state";
            public static final String UPDATE_STATUS = "error.role.update_status";
            public static final String EXIST = "error.role.exist";
            public static final String NOT_EXIST = "error.role.not_exist";
            public static final String CODE_BLANK = "error.role.code_blank";
            public static final String IDENTIFICATION_BLANK = "error.role.identification_blank";
        }
        public static final class ORDER_PREFIX {
            public static final String CREATE = "error.order_prefix.create";
            public static final String UPDATE = "error.order_prefix.update";
            public static final String UPDATE_STATE = "error.order_prefix.update_state";
            public static final String UPDATE_STATUS = "error.order_prefix.update_status";
            public static final String EXIST = "error.order_prefix.exist";
            public static final String NOT_EXIST = "error.order_prefix.not_exist";
            public static final String EXCEED_ALLOWABLE = "error.order_prefix.exceed_the_allowable_limit";
        }
        public static final class SERVICE_FEE {
            public static final String CREATE = "error.service_fee.create";
            public static final String UPDATE = "error.service_fee.update";
            public static final String UPDATE_STATE = "error.service_fee.update_state";
            public static final String UPDATE_STATUS = "error.service_fee.update_status";
            public static final String EXIST = "error.service_fee.exist";
            public static final String NOT_EXIST = "error.service_fee.not_exist";
            public static final String EXCEED_ALLOWABLE = "error.service_fee.exceed_the_allowable_limit";
        }
    }
}
