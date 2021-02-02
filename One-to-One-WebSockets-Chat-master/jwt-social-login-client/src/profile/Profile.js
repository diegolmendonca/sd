import React, { useEffect,useState } from "react";
import { Card, Avatar, notification } from "antd";
import { useRecoilState } from "recoil";
import { loggedInUser } from "../atom/globalState";
import { LogoutOutlined } from "@ant-design/icons";
import { getCurrentUser,updateUser } from "../util/ApiUtil";
import "./Profile.css";

import { Form, Input, Button } from "antd";
import { DingtalkOutlined } from "@ant-design/icons";


const { Meta } = Card;

const Profile = (props) => {
  const [currentUser, setLoggedInUser] = useRecoilState(loggedInUser);
  const [setLoading] = useState(false);

  useEffect(() => {
    if (localStorage.getItem("accessToken") === null) {
      props.history.push("/login");
    }
    loadCurrentUser();
  }, []);

  const loadCurrentUser = () => {
    getCurrentUser()
      .then((response) => {
        setLoggedInUser(response);
      })
      .catch((error) => {
        console.log(error);
      });
  };

  const logout = () => {
    localStorage.removeItem("accessToken");
    props.history.push("/login");
  };


  const onFinish = (values) => {
    updateUser(values, currentUser.id)
      .then((response) => {
        notification.success({
          message: "Success",
          description:
            "Thank you! You're successfully registered. Please Login to continue!",
        });
        props.history.push("/profile");
        setLoading(false);
      })
      .catch((error) => {
        notification.error({
          message: "Error",
          description:
            error.message || "Sorry! Something went wrong. Please try again!",
        });
      });
  };

  return (
    
    <div>
    <div className="profile-container">
      <Card
        style={{ width: 420, border: "1px solid #e1e0e0" }}
        actions={[<LogoutOutlined onClick={logout} />]}
      >
        <Meta
          avatar={
            <Avatar
              src={currentUser.profilePicture}
              className="user-avatar-circle"
            />
          }
          title={currentUser.name}
          description={"@" + currentUser.username}
        />
      </Card>
    </div>


    <div className="login-container">
      <DingtalkOutlined style={{ fontSize: 50 }} />
      <Form
        name="normal_login"
        className="login-form"
        initialValues={{ remember: true }}
        onFinish={onFinish}
      >
        <Form.Item
          name="profilePicUrl"
          rules={[
            {
              required: true,
              message: "Please input your profile picture URL!",
            },
          ]}
        >
          <Input size="large" placeholder="Profile picture url" />
        </Form.Item>
        <Form.Item>
          <Button
            shape="round"
            size="large"
            htmlType="submit"
            className="login-form-button"
          >
            Update
          </Button>
        </Form.Item>
      </Form>
    </div>


</div>











  );
};

export default Profile;
