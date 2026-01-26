packer {
  required_plugins {
    amazon = {
      version = ">= 1.2.8"
      source  = "github.com/hashicorp/amazon"
    }
    ansible = {
      version = ">= 1.1.0"
      source  = "github.com/hashicorp/ansible"
    }
  }
}

source "amazon-ebs" "java_app" {
  ami_name      = "java-app-{{timestamp}}"
  instance_type = "t3.micro"
  region        = "eu-north-1"
  source_ami    = "ami-0c7217cdde317cfec" # Ubuntu 22.04 LTS in eu-north-1
  ssh_username  = "ubuntu"
}

build {
  sources = ["source.amazon-ebs.java_app"]

  provisioner "ansible" {
    playbook_file = "./deploy.yml"
    user          = "ubuntu"
    use_proxy     = false
    ansible_env_vars = [
      "ANSIBLE_HOST_KEY_CHECKING=False",
      "ANSIBLE_PYTHON_INTERPRETER=/usr/bin/python3"
    ]
  }
}
